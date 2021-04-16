package ru.d3st.movieapp.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.network.Resource
import ru.d3st.movieapp.repository.MoviesRepository
import ru.d3st.movieapp.utils.Status
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModel() {


    // Backing property to avoid state updates from other classes
    // private val _uiState = MutableStateFlow(MoviesUiState.Success(emptyList()))
    private val _uiState =
        MutableStateFlow<Resource<List<Movie>>>(Resource.InProgress)

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<Resource<List<Movie>>> = _uiState

    init {
        fetchMovies()
        getMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            moviesRepository.fetchMovies()
                .collect {
                    _uiState.value = it
                }
        }
    }

    private fun getMovies() {
        viewModelScope.launch {
            moviesRepository.getNowPlayingMovieFromCache()
                .filterNotNull()
                // Update View with the now playing movie in Cinema
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect {
                    if (!it.isNullOrEmpty()) {
                        Timber.i("MoviesViewModel Success $it")
                        _uiState.value = Resource.Success(Status.SUCCESS, it)
                    }

                }


        }

    }
}






