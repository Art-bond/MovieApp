package ru.d3st.movieapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import ru.d3st.movieapp.database.DatabaseMovie
import ru.d3st.movieapp.database.MovieDao
import ru.d3st.movieapp.database.asDomainModel
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.network.*
import ru.d3st.movieapp.repository.baseRepositories.BaseMovieRepository
import ru.d3st.movieapp.utils.Status
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val remote: MovieRemoteDataSource,
) : BaseMovieRepository {


    //fetch movie from network and add their in db
    fun fetchMovies(): Flow<Resource<List<Movie>>?> {
        return flow {
            when (val resource = remote.getMoviesNovPlay()) {
                is Resource.Success -> {
                    val dbList: List<DatabaseMovie> =
                        movieDao.getMoviesFlow().first()
                    removeFinishedMovies(resource.data, dbList)
                    saveInCache(resource.data)
                }
                is Resource.Failure -> {
                    emit(Resource.Failure(Status.ERROR, resource.message))
                }
                Resource.InProgress -> emit(Resource.InProgress)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun newMovies():Flow<List<Movie>>{
        return  flow{
            val dbList: List<DatabaseMovie> =
                movieDao.getMoviesFlow()
                    .first().filter { it.nowPlayed }
            when (val networkList = remote.getMoviesNovPlay()){
                is Resource.Success ->
                    emit(compareNowPlayedMovies(networkList.data, dbList).asDomainModel())
            }
        }.flowOn(Dispatchers.IO)

    }


    fun getNowPlayingMovieFromCache(): Flow<List<Movie>> {
        Timber.i("Repo get data from cache")
        return movieDao.getMoviesFlow()
            .map { movies -> movies.filter { it.nowPlayed } }
            .map { it.asDomainModel() }
            .flowOn(Dispatchers.IO)
    }


    override suspend fun getMovie(movieId: Int): Movie = withContext(Dispatchers.IO) {
        val movie = movieDao.getMovie(movieId)
        return@withContext listOf(movie).asDomainModel().first()
    }


    override fun getActorsMovie(actorId: Int): Flow<Resource<List<DatabaseMovie>>?> {
        return flow{
            when(val moviesList = remote.getActorsMovies(actorId)){
                is Resource.Success -> {
                    saveInCache(moviesList.data)
                    emit(moviesList)
                }
                is Resource.Failure -> {
                    emit(Resource.Failure(Status.ERROR, moviesList.message))
                }
                Resource.InProgress -> emit(Resource.InProgress)
            }

        }.flowOn(Dispatchers.IO)
    }

    private suspend fun removeFinishedMovies(
        newMovieList: List<DatabaseMovie>,
        oldMovieList: List<DatabaseMovie>
    ) {

        val oldMinusNewIds =
            oldMovieList.map { it.movieId }.asSequence().minus(newMovieList.map { it.movieId })
        val oldMinusNewList = oldMovieList.filter { it.movieId in oldMinusNewIds }
        oldMinusNewList.forEach { it.nowPlayed = false }

        movieDao.updateAll(oldMinusNewList)
    }


    private suspend fun compareNowPlayedMovies(
        newMovieList: List<DatabaseMovie>,
        oldMovieList: List<DatabaseMovie>
    ): List<DatabaseMovie> =
        withContext(Dispatchers.IO) {

            val diffNewMovieIds =
                newMovieList.map { it.movieId }.asSequence().minus(oldMovieList.map { it.movieId })

            val diffList: List<DatabaseMovie> =
                newMovieList.filter { it.movieId in diffNewMovieIds }
            Timber.i(
                "MovieRepository with WorkManager dist list have data ${diffList.size}"
            )
            return@withContext diffList
        }

    override fun saveInCache(movies: List<DatabaseMovie>) {
        Timber.i("movies repository add to DB ${movies.size}")
        movieDao.insertAll(movies)
    }
}
















