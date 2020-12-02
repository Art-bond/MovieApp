package ru.d3st.academyandroid.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.d3st.academyandroid.domain.Movie
import java.lang.IllegalArgumentException

class MovieDetailsVIewModelFactory (
    private val selectedMoview: Movie,
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
                return MovieDetailsViewModel(selectedMoview) as T
            }
            throw  IllegalArgumentException("Unknown ViewModel Class")
        }

    }