package ru.d3st.movieapp.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.repository.MoviesRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    ViewModel() {


    // Backing property to avoid state updates from other classes
    // private val _uiState = MutableStateFlow(MoviesUiState.Success(emptyList()))
    private val _uiState =
        MutableStateFlow<MoviesUiState<List<Movie>>>(MoviesUiState.InProgress())

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<MoviesUiState<List<Movie>>> = _uiState


    init {
        fetchMovies()
        getMovies()
    }

    private fun fetchMovies() {
        _uiState.value = MoviesUiState.InProgress()
        viewModelScope.launch {
            moviesRepository.fetchMovies()
                .collect {
                    if (it != null) {
                        _uiState.value = it
                    }
                }
        }
    }

    private fun getMovies() {
        viewModelScope.launch {
            moviesRepository.getNowPlayingMovieFromCache()
                .collect {
                    if (!it.isNullOrEmpty()) {
                        Timber.i("MoviesViewModel Success $it")
                        _uiState.value = MoviesUiState.Success(it)
                    }
                }
        }
    }

    fun retry() {
        Timber.i("MovieListViewModel retry button is clicked")
        fetchMovies()
    }
}






