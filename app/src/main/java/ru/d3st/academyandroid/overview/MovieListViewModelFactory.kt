package ru.d3st.academyandroid.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.d3st.academyandroid.repository.MoviesRepository

class MovieListViewModelFactory (private val moviesRepository: MoviesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieListViewModel(moviesRepository) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }

}