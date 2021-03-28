package ru.d3st.movieapp.overview

import ru.d3st.movieapp.domain.Movie

sealed class MoviesUiState {
    data class Success(val movies: List<Movie>): MoviesUiState()
    data class Error(val exception: Throwable): MoviesUiState()
}
