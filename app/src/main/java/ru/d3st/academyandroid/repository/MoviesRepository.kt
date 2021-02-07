package ru.d3st.academyandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.database.MovieActorCrossRef
import ru.d3st.academyandroid.database.MoviesDataBase
import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.*
import ru.d3st.academyandroid.network.tmdb.ResponseActorsContainer
import ru.d3st.academyandroid.network.tmdb.ResponseMovieActorsContainer
import ru.d3st.academyandroid.network.tmdb.ResponseMovieContainer
import timber.log.Timber

class MoviesRepository(private val dataBase: MoviesDataBase) {


    val movies: LiveData<List<Movie>> = Transformations.map(dataBase.movieDao.getMovies()) {
        it.asDomainModel()
    }


/*    suspend fun actors(movieId: Int): List<Actor> =
        withContext(Dispatchers.IO) {
            return@withContext dataBase.actorDao.getActors(movieId).asDomainModel()
        }*/

    suspend fun getMovie(movieId: Int): Movie = withContext(Dispatchers.IO) {

        val movie = dataBase.movieDao.getMovie(movieId)
        return@withContext listOf(movie).asDomainModel().first()
    }


    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh movies is called")
            val responseGenres = MovieApi.retrofitService.getGenres().genres
            val genres: Map<Int, Genre> = responseGenres.associateBy { it.id }
            val movieList: ResponseMovieContainer
            movieList = try {
                MovieApi.retrofitService.getNovPlayingMovie()
            } catch (e: Exception) {
                Timber.i(e)
                ResponseMovieContainer(0, ArrayList(), 1, 0)
            }

            Timber.i("movie list have data $movieList")
            dataBase.movieDao.insertAll(movieList.asDatabaseModel(genres))
        }
    }

    suspend fun refreshActorsBioMovie(actorId: Int) {
        withContext(Dispatchers.IO) {
            val responseGenres = MovieApi.retrofitService.getGenres().genres
            val genres: Map<Int, Genre> = responseGenres.associateBy { it.id }
            val movieList: ResponseMovieActorsContainer = try {
                MovieApi.retrofitService.getActorsMovies(actorId)
            } catch (e: java.lang.Exception) {
                ResponseMovieActorsContainer(emptyList(), emptyList(), -1)
            }
            dataBase.movieDao.insertAll(movieList.asDataBaseModel(genres))

        }
    }

    suspend fun refreshMovieWithActors(movieId: Int) {
        withContext(Dispatchers.IO) {
            Timber.i("Response actor is called")
            val actors: ResponseActorsContainer = try {
                MovieApi.retrofitService.getActors(movieId)
            } catch (e: Exception) {
                Timber.i("exception on response Actor List")
                ResponseActorsContainer(0, ArrayList())
            }
            Timber.i("actors list contained ${actors.asDataBaseActorModel().size}")

            dataBase.actorDao.insertMovieWithActors(
                actors = actors.asDataBaseActorModel(),
                movieWithActors = actors.asMovieActorCross(movieId)
            )
        }
    }

    suspend fun actors(movieId: Int): List<Actor> = withContext(Dispatchers.IO) {
        return@withContext dataBase.actorDao.getActorsTheMovie(movieId).actors.asDomainModel()

    }


}
















