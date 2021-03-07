package ru.d3st.academyandroid.actor

import android.annotation.SuppressLint
import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.domain.ActorBio
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.repository.ActorBioRepository
import ru.d3st.academyandroid.repository.MoviesRepository
import ru.d3st.academyandroid.utils.Status
import timber.log.Timber

@SuppressLint("NullSafeMutableLiveData")
class ActorBioViewModel @AssistedInject constructor(
    @Assisted actorId: Int,
    private val actorBioRepository: ActorBioRepository,
    private val moviesRepository: MoviesRepository,
) : ViewModel() {

    private val disposable = CompositeDisposable()

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

    private fun getMoviesData(actorId: Int) {
        Timber.i("ActorBioMovie fetch is Started")

        disposable.add(moviesRepository.getMoviesByActor(actorId)
            .subscribe(
                {
                    _actorsMovies.value = it
                    Timber.e("ActorBio has Success ${it.size}")
                },
                {
                    Timber.e("ActorBio has Error ${it.localizedMessage}")
                    _errorMessage.value = it.localizedMessage
                },
                {
                    Timber.i("ActorBio Stream completed")
                }
            ))
        Timber.i("ActorBioMovie fetch is Finished")


    }

    private fun getActorData(actorId: Int) {
        Timber.i("ActorBio fetch is  Started")
        val resource = actorBioRepository.getActorBio(actorId)

        disposable.add(resource
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                Timber.i("ActorBioRepository getActorBio Loading")
                _statusResource.value = Status.LOADING

            }
            .subscribe(
                {
                    _actorsBio.value = it
                    _statusResource.value = Status.SUCCESS
                },
                {
                    Timber.e("ActorBioRepository getActorBio error ${it.localizedMessage}")
                    _errorMessage.value = it.message
                    _statusResource.value = Status.ERROR
                }))
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
            actorId: Int,
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


