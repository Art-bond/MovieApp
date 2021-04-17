package ru.d3st.movieapp.repository.baseRepositories

import kotlinx.coroutines.flow.Flow
import ru.d3st.movieapp.database.DatabaseMovie
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.network.Resource

interface BaseMovieRepository {

    suspend fun getMovie(movieId: Int): Movie

    fun getActorsMovie(actorId: Int): Flow<Resource<List<DatabaseMovie>>?>

    fun saveInCache(movies: List<DatabaseMovie>)
}