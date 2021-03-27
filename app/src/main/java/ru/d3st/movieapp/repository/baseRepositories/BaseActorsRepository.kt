package ru.d3st.movieapp.repository.baseRepositories

import ru.d3st.movieapp.domain.Actor

interface BaseActorsRepository {
    suspend fun refreshMovieWithActors(movieId: Int)

    suspend fun getActors(movieId: Int): List<Actor>
}