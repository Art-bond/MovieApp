package ru.d3st.movieapp.repository.baseRepositories

import androidx.lifecycle.LiveData
import ru.d3st.movieapp.database.DatabaseMovie
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.network.Resource

interface BaseMovieRepository {

    val movies: LiveData<List<Movie>>

    suspend fun getMovie(movieId: Int): Movie

    suspend fun getActorsMovie(actorId: Int): List<Movie>

    suspend fun refreshMovies(): List<DatabaseMovie>

    fun saveInCache(movies: List<DatabaseMovie>)
    suspend fun fetchMovies(resource: Resource<List<DatabaseMovie>>): List<Movie>
}