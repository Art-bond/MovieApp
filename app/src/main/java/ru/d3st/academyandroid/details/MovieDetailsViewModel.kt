package ru.d3st.academyandroid.details

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.repository.ActorsRepository
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber

class MovieDetailsViewModel @AssistedInject constructor(
    private val moviesRepository: MoviesRepository,
    private val actorsRepository: ActorsRepository,
    @Assisted private val movieId: Int
) : ViewModel() {

    val actorsResource = actorsRepository.actors

    private var _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie


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

    private val subscriptions = CompositeDisposable()


    init {
        getDetailFromDataBase()
        refreshDataFromRepository()

    }

    private fun getDetailFromDataBase() {
            moviesRepository.getMovieDetail(movieId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _movie.value = it
                    },
                    {
                        Timber.e("Detail DataBase error. ${it.localizedMessage}")
                        _eventNetworkError.value = true
                    }
                ).addTo(subscriptions)



    }

    private fun refreshDataFromRepository() {
            actorsRepository.fetchActorsInMovie(movieId)
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



