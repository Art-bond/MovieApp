package ru.d3st.academyandroid.details

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.database.getDatabase
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.tmdb.ResponseActorsContainer
import ru.d3st.academyandroid.network.MovieApi
import ru.d3st.academyandroid.network.asDomainActorModel
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber
import java.io.IOException

class MovieDetailsViewModel(
    application: Application,
    private val movieId: Int
) : ViewModel() {

    private val moviesRepository = MoviesRepository(getDatabase(application))

    /**
     * The data source this ViewModel will fetch results from.
     */


    private var _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>>
        get() = _actors


    init {
        getMovieDetail()
        //getActorTask(movieId)
        refreshDataFromRepository()
    }

    private fun getMovieDetail() {
        viewModelScope.launch {
            val selectedMovie = moviesRepository.getMovie(movieId)
            _movie.value = selectedMovie

        }
    }


    private fun getActorTask(selectedMovieId: Int) =
        viewModelScope.launch {
            val actors = getActorList(selectedMovieId)
           // onPostExecute(actors)

        }
/*
    private fun onPostExecute(result: ResponseActorsContainer) {
        _actors.value = result.asDomainActorModel()

    }*/

    private suspend fun getActorList(selectedMovieId: Int): ResponseActorsContainer =
        withContext(Dispatchers.IO) {
            try {
                Timber.i("waiting response with $selectedMovieId")
                val response = MovieApi.retrofitService.getActors(selectedMovieId)
                Timber.i("Response actors is $response")
                return@withContext response
            } catch (e: Exception) {
                Timber.e("Response actors exception is $e")

                return@withContext ResponseActorsContainer(0, emptyList())
            }
        }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                moviesRepository.refreshMovieWithActors(movieId)
                _actors.value = moviesRepository.actors(movieId)
                //_eventNetworkError.value = false
                //_isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                //if (playlist.value.isNullOrEmpty())
                 //   _eventNetworkError.value = true
            }
        }
    }

}


