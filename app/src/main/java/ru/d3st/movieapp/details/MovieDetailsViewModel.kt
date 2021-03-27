package ru.d3st.movieapp.details

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.d3st.movieapp.domain.Actor
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.repository.ActorsRepository
import ru.d3st.movieapp.repository.MoviesRepository

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
        getThisMovieDetail()
        refreshDataFromRepository()

    }

    private fun getThisMovieDetail() {
        viewModelScope.launch {
            val selectedMovie = moviesRepository.getMovie(movieId)
            _movie.value = selectedMovie

        }
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
            _actors.value = actorsRepository.getActors(movieId)
            if (actors.value.isNullOrEmpty())
                _eventNetworkError.value = true
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



