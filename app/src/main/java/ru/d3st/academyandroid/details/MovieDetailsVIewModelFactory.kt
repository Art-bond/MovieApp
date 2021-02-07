package ru.d3st.academyandroid.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.d3st.academyandroid.database.MovieDao
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.repository.MoviesRepository
import java.lang.IllegalArgumentException

class MovieDetailsVIewModelFactory (
private val application: Application,
private val movieId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
                return MovieDetailsViewModel(application, movieId) as T
            }
            throw  IllegalArgumentException("Unknown ViewModel Class")
        }

    }