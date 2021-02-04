package ru.d3st.academyandroid.network.tmdb

import ru.d3st.academyandroid.domain.Genre


data class ResponseGenreContainer(
    val genres: List<Genre>
)