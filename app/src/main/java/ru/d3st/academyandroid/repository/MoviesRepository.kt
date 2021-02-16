package ru.d3st.academyandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.database.MoviesDataBase
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
class MoviesRepository @Inject constructor(private val dataBase: MoviesDataBase) {


    val movies: LiveData<List<Movie>> = Transformations.map(dataBase.movieDao.getMovies()) {
        it.asDomainModel()
    }



    val moviesNowPlayed: Flow<List<Movie>> = dataBase.movieDao.getNovPlayingMovies()
        .map { movies ->
            movies.filter { it.nowPlayed }.asDomainModel()
        }


    suspend fun getMovie(movieId: Int): Movie = withContext(Dispatchers.IO) {

        val movie = dataBase.movieDao.getMovie(movieId)
        return@withContext listOf(movie).asDomainModel().first()
    }


    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh movies is called")
            val responseGenres = MovieApi.retrofitService.getGenres().genres
            val genres: Map<Int, Genre> = responseGenres.associateBy { it.id }
            val movieList: ResponseMovieContainer = try {
                MovieApi.retrofitService.getNovPlayingMovie()
            } catch (e: Exception) {
                showNetworkError(e)
                //ResponseMovieContainer(0, ArrayList(), 1, 0)
            }

            Timber.i("movie list have data $movieList")
            dataBase.movieDao.insertNowPlayingMovies(movieList.asDatabaseModelNowPlayed(genres))
        }
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
            dataBase.movieDao.insertAll(movieList.asDataBaseModel(genres))

        }
    }
}
















