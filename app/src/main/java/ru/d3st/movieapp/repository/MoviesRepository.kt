package ru.d3st.movieapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.d3st.movieapp.database.DatabaseMovie
import ru.d3st.movieapp.database.MovieDao
import ru.d3st.movieapp.database.asDomainModel
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.network.*
import ru.d3st.movieapp.network.tmdb.ResponseMovieContainer
import ru.d3st.movieapp.repository.baseRepositories.BaseMovieRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val movieApi: MovieApi
) : BaseMovieRepository {


    override val movies: LiveData<List<Movie>> = Transformations.map(movieDao.getMoviesFlow().asLiveData()) {
        it.asDomainModel()
    }

    override val moviesNowPlayed: Flow<List<Movie>> = movieDao.getMoviesFlow()
        .map { movies ->
            movies.filter { it.nowPlayed }.asDomainModel()
        }

    override suspend fun getMovie(movieId: Int): Movie = withContext(Dispatchers.IO) {
        val movie = movieDao.getMovie(movieId)
        return@withContext listOf(movie).asDomainModel().first()
    }


    override suspend fun getActorsMovie(actorId: Int):List<Movie> {
        val resource = safeApiCall(Dispatchers.IO) {
            movieApi.networkService.getActorsMovies(actorId).asDataBaseModel(getGenres())
        }
        return fetchMoviesDataBase(resource)
    }

    private suspend fun fetchMoviesDataBase(resource: Resource<List<DatabaseMovie>>): List<Movie> =
        withContext(Dispatchers.IO)
        {
            when (resource) {
                is Resource.Success -> {
                    movieDao.insertAll(resource.data)
                    resource.data.asDomainModel()
                }
                else -> emptyList()
            }
        }


    override suspend fun refreshMovies() =
        withContext(Dispatchers.IO) {
            Timber.d("refresh movies is called")
            val genres = getGenres()
            //list contains movie that now playing in cinema
            val loadMoviesList = try {
                movieApi.networkService.getNovPlayingMovie()
            } catch (e: Exception) {
                showNetworkError(e)
            }
            Timber.i(
                "MovieRepository with WorkManager movie list have data ${loadMoviesList.movies.size}"
            )
            //list contains movies that were not in the database before the update
            val newMovies = compareNowPlayedMovies(loadMoviesList.asDatabaseModelNowPlayed(genres))
            //marks movies that are no longer going to the cinema as nowPlayed=false
            removeNoLongerGoingMovieFromDataBase(loadMoviesList.asDatabaseModelNowPlayed(genres))
            //add and replace now playing movie in database
            movieDao.insertNowPlayingMovies(loadMoviesList.asDatabaseModelNowPlayed(genres))
            return@withContext newMovies
        }

    private suspend fun getGenres() =
        movieApi.networkService.getGenres().genres.associateBy { it.id }

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


    private fun showNetworkError(e: Exception): ResponseMovieContainer {
        Timber.e("Error on request movie list: $e")
        return ResponseMovieContainer(0, ArrayList(), 1, 0)
    }
}
















