package ru.d3st.academyandroid.overview

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.database.DatabaseMovie
import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.database.getDatabase

import ru.d3st.academyandroid.domain.*

import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class MovieListViewModel(application: Application) : ViewModel() {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val moviesRepository = MoviesRepository(getDatabase(application))

    private val moviesList = moviesRepository.moviesNowPlayed.asLiveData()

    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>>
        get() = _genres

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

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
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    /**
     * A playlist of videos displayed on the screen.
     */
    private val _movies = moviesList
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    init {
        refreshDataFromRepository()
    }

    fun displayMovieDetailsBegin(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    fun displaySelectedMovieComplete() {
        _navigateToSelectedMovie.value = null
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                moviesRepository.refreshMovies()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (moviesList.value.isNullOrEmpty())
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




