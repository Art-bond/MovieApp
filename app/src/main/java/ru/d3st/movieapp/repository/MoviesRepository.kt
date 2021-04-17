package ru.d3st.movieapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import ru.d3st.movieapp.database.DatabaseMovie
import ru.d3st.movieapp.database.MovieDao
import ru.d3st.movieapp.database.asDomainModel
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.network.*
import ru.d3st.movieapp.overview.MoviesUiState
import ru.d3st.movieapp.repository.baseRepositories.BaseMovieRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val remote: MovieRemoteDataSource,
) : BaseMovieRepository {


    override val movies: LiveData<List<Movie>> =
        Transformations.map(movieDao.getMoviesFlow().asLiveData()) {
            it.asDomainModel()
        }

    fun fetchMovies(): Flow<MoviesUiState<List<Movie>>?> {
        return flow {
            when (val resource = remote.getMovies()) {
                is Resource.Success -> {
                    saveInCache(resource.data)
                }
                is Resource.Failure -> {
                    emit(MoviesUiState.Failure<List<Movie>>(resource.message!!))
                }
                Resource.InProgress -> emit(MoviesUiState.InProgress<List<Movie>>())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getNowPlayingMovieFromCache(): Flow<List<Movie>> {
        Timber.i("Repo get data from cache")
        return movieDao.getMoviesFlow()
            .map { movies-> movies.filter { it.nowPlayed } }
            .map { it.asDomainModel() }
            .flowOn(Dispatchers.IO)
    }


    override suspend fun getMovie(movieId: Int): Movie = withContext(Dispatchers.IO) {
        val movie = movieDao.getMovie(movieId)
        return@withContext listOf(movie).asDomainModel().first()
    }


    override fun saveInCache(movies: List<DatabaseMovie>) {
        Timber.i("movies repository add to DB ${movies.size}")
        movieDao.insertAll(movies)
    }


    override suspend fun getActorsMovie(actorId: Int): List<Movie> {
        return fetchMovies(remote.getActors(actorId))
    }

    override suspend fun fetchMovies(resource: Resource<List<DatabaseMovie>>): List<Movie> =
        withContext(Dispatchers.IO)
        {
            when (resource) {
                is Resource.Success -> {
                    saveInCache(resource.data)
                    resource.data.asDomainModel()
                }
                is Resource.Failure -> {
                    Timber.e("fetch movie error ${resource.message}")
                    emptyList()
                }
                Resource.InProgress -> {
                    Timber.i("fetch movie loading ")
                    emptyList()
                }
            }
        }


    override suspend fun refreshMovies() =
        withContext(Dispatchers.IO) {
            Timber.d("refresh movies is called")
            //list contains movie that now playing in cinema
            when (val loadMoviesList = remote.getMovies()) {
                is Resource.Success -> {
                    //list contains movies that were not in the database before the update
                    val newMovies = compareNowPlayedMovies(loadMoviesList.data)
                    //marks movies that are no longer going to the cinema as nowPlayed=false
                    removeNoLongerGoingMovieFromDataBase(loadMoviesList.data)
                    //add and replace now playing movie in database
                    movieDao.insertNowPlayingMovies(loadMoviesList.data)

                    return@withContext newMovies

                }
                else -> return@withContext emptyList()
            }
        }

    private suspend fun removeNoLongerGoingMovieFromDataBase(newMovieList: List<DatabaseMovie>) {
        val oldMovieList: List<DatabaseMovie> =
            movieDao.getMoviesSync().filter { it.nowPlayed }
        val oldMinusNewIds =
            oldMovieList.map { it.movieId }.asSequence().minus(newMovieList.map { it.movieId })
        val oldMinusNewList = oldMovieList.filter { it.movieId in oldMinusNewIds }
        oldMinusNewList.forEach { it.nowPlayed = false }

        movieDao.updateAll(oldMinusNewList)
    }


    private suspend fun compareNowPlayedMovies(newMovieList: List<DatabaseMovie>): List<DatabaseMovie> =
        withContext(Dispatchers.IO) {
            val oldMovieList: List<DatabaseMovie> =
                movieDao.getMoviesSync().filter { it.nowPlayed }
            val diffNewMovieIds =
                newMovieList.map { it.movieId }.asSequence().minus(oldMovieList.map { it.movieId })

            val diffList: List<DatabaseMovie> =
                newMovieList.filter { it.movieId in diffNewMovieIds }
            Timber.i(
                "MovieRepository with WorkManager dist list have data ${diffList.size}"
            )
            return@withContext diffList
        }

}
















