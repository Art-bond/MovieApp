package ru.d3st.movieapp.repository.baseRepositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.d3st.movieapp.database.DatabaseMovie
import ru.d3st.movieapp.domain.Movie

interface BaseMovieRepository {
    val movies: LiveData<List<Movie>>
    val moviesNowPlayed: Flow<List<Movie>>

    suspend fun getMovie(movieId: Int): Movie

    suspend fun getActorsMovie(actorId: Int): List<Movie>

    suspend fun refreshMovies(): List<DatabaseMovie>
}