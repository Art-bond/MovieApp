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
import ru.d3st.academyandroid.repository.ActorsRepository
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber
import java.io.IOException

class MovieDetailsViewModel(
    application: Application,
    private val movieId: Int
) : ViewModel() {

    private val moviesRepository = MoviesRepository(getDatabase(application))
    private val actorsRepository = ActorsRepository(getDatabase(application))


    private var _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>>
        get() = _actors

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown



    init {
        getMovieDetail()
        refreshDataFromRepository()
    }

    private fun getMovieDetail() {
        viewModelScope.launch {
            val selectedMovie = moviesRepository.getMovie(movieId)
            _movie.value = selectedMovie

        }
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                actorsRepository.refreshMovieWithActors(movieId)
                _actors.value = actorsRepository.actors(movieId)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (actors.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

}


