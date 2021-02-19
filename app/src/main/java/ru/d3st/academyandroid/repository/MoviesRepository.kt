package ru.d3st.academyandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.database.DatabaseMovie
import ru.d3st.academyandroid.database.MovieDao
import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.*
import ru.d3st.academyandroid.network.tmdb.ResponseMovieActorsContainer
import ru.d3st.academyandroid.network.tmdb.ResponseMovieContainer
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(private val movieDao: MovieDao) {


    val movies: LiveData<List<Movie>> = Transformations.map(movieDao.getMovies()) {
        it.asDomainModel()
    }


    val moviesNowPlayed: Flow<List<Movie>> = movieDao.getMoviesFlow()
        .map { movies ->
            movies.filter { it.nowPlayed }.asDomainModel()
        }


    suspend fun getMovie(movieId: Int): Movie = withContext(Dispatchers.IO) {

        val movie = movieDao.getMovie(movieId)
        return@withContext listOf(movie).asDomainModel().first()
    }


    suspend fun refreshMovies() =
        withContext(Dispatchers.IO) {
            Timber.d("refresh movies is called")
            val responseGenres = MovieApi.retrofitService.getGenres().genres
            val genres: Map<Int, Genre> = responseGenres.associateBy { it.id }
            val movieList: ResponseMovieContainer = try {
                MovieApi.retrofitService.getNovPlayingMovie()
            } catch (e: Exception) {
                showNetworkError(e)
            }
            Timber.i(
                "MovieRepository with WorkManager movie list have data ${movieList.movies.size}"
            )
            val compareList = compareNowPlayedMovies(movieList.asDatabaseModelNowPlayed(genres))
            updateNowPlayedMovieInDataBase()
            movieDao.insertNowPlayingMovies(movieList.asDatabaseModelNowPlayed(genres))
            return@withContext compareList
        }

    private suspend fun updateNowPlayedMovieInDataBase() {
        val beforeRefreshList: List<DatabaseMovie> =
            movieDao.getMoviesSync().filter { it.nowPlayed }
        beforeRefreshList.forEach { it.nowPlayed = false }
        Timber.i(
            "MovieRepository with WorkManager refresh and nowPlayedList contains ${beforeRefreshList.size}"
        )
        movieDao.updateAll(beforeRefreshList)
    }


    private suspend fun compareNowPlayedMovies(newMovieList: List<DatabaseMovie>): List<DatabaseMovie> =
        withContext(Dispatchers.IO) {
            val oldMovieList: List<DatabaseMovie> =
                movieDao.getMoviesSync().filter { it.nowPlayed }
            val diffMovieIds =
                newMovieList.map { it.movieId }.asSequence().minus(oldMovieList.map { it.movieId })
            val diffList: List<DatabaseMovie> = newMovieList.filter { it.movieId in diffMovieIds }
            Timber.i(
                "MovieRepository with WorkManager dist list have data ${diffList.size}"
            )
            return@withContext diffList
        }


    private fun showNetworkError(e: Exception): ResponseMovieContainer {
        Timber.e("Error on request movie list: $e")
        return ResponseMovieContainer(0, ArrayList(), 1, 0)
    }

    suspend fun refreshActorsBioMovie(actorId: Int) {
        withContext(Dispatchers.IO) {
            val responseGenres = MovieApi.retrofitService.getGenres().genres
            val genres: Map<Int, Genre> = responseGenres.associateBy { it.id }
            val movieList: ResponseMovieActorsContainer = try {
                MovieApi.retrofitService.getActorsMovies(actorId)
            } catch (e: Exception) {
                ResponseMovieActorsContainer(emptyList(), emptyList(), -1)
            }
            movieDao.insertAll(movieList.asDataBaseModel(genres))

        }
    }
}
















