package ru.d3st.movieapp.network

import ru.d3st.movieapp.network.tmdb.ErrorResponse
import ru.d3st.movieapp.utils.Status

// A generic class that contains data and status about loading this data.

sealed class Resource<out T> {
    data class Success<out T>(val status: Status = Status.SUCCESS, val data: T) : Resource<T>()
    data class Failure(
        val status: Status = Status.ERROR,
        val message: String?,
        val error: ErrorResponse? = null
    ) : Resource<Nothing>()
    object InProgress : Resource<Nothing>()

}