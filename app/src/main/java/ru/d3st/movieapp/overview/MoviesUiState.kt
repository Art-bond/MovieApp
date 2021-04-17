package ru.d3st.movieapp.overview

import ru.d3st.movieapp.utils.Status

sealed class MoviesUiState<T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
    ){
    class Success<T>(data: T): MoviesUiState<T>(Status.SUCCESS, data)
    class Failure<T>(message: String, data: T? = null): MoviesUiState<T>(Status.ERROR, data, message)
    class InProgress<T>(data: T? = null):MoviesUiState<T>(Status.LOADING, data)
}
