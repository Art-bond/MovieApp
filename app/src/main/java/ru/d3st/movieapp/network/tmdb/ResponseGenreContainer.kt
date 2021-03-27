package ru.d3st.movieapp.network.tmdb

import ru.d3st.movieapp.domain.Genre


data class ResponseGenreContainer(
    val genres: List<Genre>
)