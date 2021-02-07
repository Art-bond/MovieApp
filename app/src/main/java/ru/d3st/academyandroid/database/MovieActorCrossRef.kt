package ru.d3st.academyandroid.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["movieId", "actorId"])
data class MovieActorCrossRef(
    val movieId: Int,
    val actorId: Int
)