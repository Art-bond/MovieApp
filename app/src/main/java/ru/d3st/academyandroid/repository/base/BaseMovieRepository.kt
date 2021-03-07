package ru.d3st.academyandroid.repository.base

import io.reactivex.Observable
import ru.d3st.academyandroid.domain.Movie

interface BaseMovieRepository {
    fun getMovieDetail(movieId: Int): Observable<Movie>

    fun getMoviesByActor(actorId: Int): Observable<List<Movie>>

    fun refreshMovies()
}