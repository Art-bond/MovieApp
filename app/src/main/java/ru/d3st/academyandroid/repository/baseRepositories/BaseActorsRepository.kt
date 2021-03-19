package ru.d3st.academyandroid.repository.baseRepositories

import ru.d3st.academyandroid.domain.Actor

interface BaseActorsRepository {
    suspend fun refreshMovieWithActors(movieId: Int)

    suspend fun getActors(movieId: Int): List<Actor>
}