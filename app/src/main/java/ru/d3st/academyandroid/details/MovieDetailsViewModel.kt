package ru.d3st.academyandroid.details

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.repository.ActorsRepository
import ru.d3st.academyandroid.repository.MoviesRepository
import java.io.IOException
import javax.inject.Inject

class MovieDetailsViewModel @AssistedInject constructor(
    private val moviesRepository: MoviesRepository,
    private val actorsRepository: ActorsRepository,
    @Assisted private val movieId: Int
) : ViewModel() {



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
    companion object {
        fun provideFactory(
            assistedFactory: MovieDetailsVIewModelFactory,
            movieId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(movieId) as T
            }
        }
    }
}



