package ru.d3st.movieapp.database

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "actorId"])
data class MovieActorCrossRef(
    val movieId: Int,
    val actorId: Int
)