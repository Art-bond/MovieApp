package ru.d3st.movieapp.actor

import android.annotation.SuppressLint
import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.d3st.movieapp.domain.ActorBio
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.network.Resource
import ru.d3st.movieapp.repository.ActorBioRepository
import ru.d3st.movieapp.repository.MoviesRepository
import ru.d3st.movieapp.utils.Status
import timber.log.Timber

@SuppressLint("NullSafeMutableLiveData")
class ActorBioViewModel @AssistedInject constructor(
    @Assisted actorId: Int,
    private val actorBioRepository: ActorBioRepository,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val actor = actorId

    private val _actorsBio = MutableLiveData<ActorBio>()
    val actorsBio: LiveData<ActorBio>
        get() = _actorsBio

    private val _actorsMovies = MutableLiveData<List<Movie>>()
    val actorsMovies: LiveData<List<Movie>>
        get() = _actorsMovies

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _statusResource = MutableLiveData<Status>()
    val statusResource: LiveData<Status>
        get() = _statusResource

    private val _navigateToMovieDetails = MutableLiveData<Movie>()
    val navigateToMovieDetails: LiveData<Movie>
        get() = _navigateToMovieDetails


    init {
        fetch()

    }

    private fun fetch() {
        viewModelScope.launch {
            getActorData(actor)
            getMoviesData(actor)
        }
    }

    private suspend fun getMoviesData(actorId: Int) {
        Timber.i("ActorBioMovie fetch is  Started")

        _actorsMovies.value = moviesRepository.getActorsMovie(actorId)
        Timber.i("ActorBioMovie fetch is  Finished")


    }

    private suspend fun getActorData(actorId: Int) {
        Timber.i("ActorBio fetch is  Started")
        _statusResource.value = Status.LOADING
        val resource = actorBioRepository.getActorBio(actorId)

        when (resource) {
            is Resource.Failure -> {
                _errorMessage.value = resource.message
                _statusResource.value = resource.status
                Timber.i("ActorBio failure status ${resource.status}")
            }
            is Resource.InProgress -> {
                Timber.i("ActorBio loading status $resource")

            }
            is Resource.Success -> {
                _actorsBio.value = resource.data
                _statusResource.value = resource.status
                Timber.i("ActorBio Successfully status ${resource.status}")

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

    fun retry() {
        fetch()
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


