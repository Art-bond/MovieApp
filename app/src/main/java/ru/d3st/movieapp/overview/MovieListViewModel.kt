package ru.d3st.movieapp.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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
    private val _uiState =
        MutableStateFlow<Resource<List<Movie>>>(Resource.Success(Status.SUCCESS, emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<Resource<List<Movie>>> = _uiState


    private val _networkState =
        MutableStateFlow<Resource<List<Movie>>>(Resource.InProgress)
    val networkState: StateFlow<Resource<List<Movie>>> = _networkState


    init {
        fetchMovies()
        getMovies()
    }

    private fun fetchMovies() {
        _networkState.value = Resource.InProgress
        viewModelScope.launch {
            moviesRepository.fetchMovies()
                .collect {
                    if (it != null) {
                        _networkState.value = it
                    }
                }
        }
    }

    private fun getMovies() {
        viewModelScope.launch {
            moviesRepository.getNowPlayingMovieFromCache()
                .collect {
                    if (!it.isNullOrEmpty()) {
                        Timber.i("MoviesViewModel Success add ${it.size} movies")
                        _uiState.value = Resource.Success(Status.SUCCESS, it)
                    }
                }
        }
    }

    fun retry() {
        fetchMovies()
    }
}






