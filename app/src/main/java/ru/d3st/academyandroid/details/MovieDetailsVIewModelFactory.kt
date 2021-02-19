package ru.d3st.academyandroid.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory
import ru.d3st.academyandroid.repository.ActorsRepository
import ru.d3st.academyandroid.repository.MoviesRepository
import java.lang.IllegalArgumentException

@AssistedFactory
interface MovieDetailsVIewModelFactory{
    fun create(movieId : Int):MovieDetailsViewModel
}
/*
class MovieDetailsVIewModelFactory (
private val moviesRepository: MoviesRepository,
private val actorsRepository: ActorsRepository,
private val movieId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
                return MovieDetailsViewModel(moviesRepository, actorsRepository, movieId) as T
            }
            throw  IllegalArgumentException("Unknown ViewModel Class")
        }

    }*/
