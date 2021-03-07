package ru.d3st.academyandroid.overview

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModel() {

    /**
     * The data source this ViewModel will fetch results from.
     */

    val moviesList = moviesRepository.movies

    /**
     * The Status current operation
     */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    /**
     * A playlist of videos displayed on the screen.
     */
/*    private val _movies =
        LiveDataReactiveStreams.fromPublisher(moviesList)
    val movies: LiveData<List<Movie>>
        get() = _movies*/

    init {
        fetchMoviesData()
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    fun fetchMoviesData() {
        try {
            moviesRepository.refreshMovies()

        } catch (networkError: IOException) {
            Timber.e("MovieListViewModel error ${networkError.localizedMessage}")
        }

    }


}




