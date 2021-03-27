package ru.d3st.movieapp.details

import dagger.assisted.AssistedFactory

@AssistedFactory
interface MovieDetailsVIewModelFactory{
    fun create(movieId : Int): MovieDetailsViewModel
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
