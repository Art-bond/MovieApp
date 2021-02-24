package ru.d3st.academyandroid.actor

import android.annotation.SuppressLint
import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.domain.ActorBio
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.MovieApi
import ru.d3st.academyandroid.network.asDomainModel
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber

class ActorBioViewModel @AssistedInject constructor(
    @Assisted actorId: Int,
    private val moviesRepository: MoviesRepository
) : ViewModel() {


    private val _actorsMovies = MutableLiveData<List<Movie>>()
    val actorsMovies: LiveData<List<Movie>>
        get() = _actorsMovies

    private val _actorsBio = MutableLiveData<ActorBio>()
    val actorsBio: LiveData<ActorBio>
        get() = _actorsBio

    private val _navigateToMovieDetails = MutableLiveData<Movie>()
    val navigateToMovieDetails: LiveData<Movie>
        get() = _navigateToMovieDetails

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
        getActorsMovieData(actorId)
        getActorBioData(actorId)
    }

    private fun getActorsMovieData(actorId: Int) {
        viewModelScope.launch {
            try {
                val responseActorsMovies = MovieApi.networkService.getActorsMovies(actorId)
                moviesRepository.refreshActorsBioMovie(actorId)
                Timber.i("ActorsMovies. actor's movie list has been loaded $responseActorsMovies")
                val genres = getGenres()
                _actorsMovies.value = responseActorsMovies.cast.asDomainModel(genres)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (e: Exception) {
                Timber.e("ActorsMovies. error is $e")
                if (actorsMovies.value.isNullOrEmpty())
                    _eventNetworkError.value = true

            }
        }
    }

    private suspend fun getGenres() =
        MovieApi.networkService.getGenres().genres.associateBy { it.id }

    private fun getActorBioData(actorId: Int) {
        viewModelScope.launch {
            try {
                val responseActorBio = MovieApi.networkService.getActorBio(actorId)
                _actorsBio.value = responseActorBio.asDomainModel()
            } catch (e: Exception) {

            }
        }
    }

    fun onMovieSelected(movie: Movie) {
        _navigateToMovieDetails.value = movie
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun onMovieNavigated() {
        _navigateToMovieDetails.value = null
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    companion object {
        fun provideFactory(
            assistedFactory: ActorBioViewModelFactory,
            actorId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(actorId) as T
            }
        }
    }
}

@AssistedFactory
interface ActorBioViewModelFactory {
    fun create(actorId: Int): ActorBioViewModel
}


