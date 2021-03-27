package ru.d3st.movieapp.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class MovieWithActors(
    @Embedded
    val movie: DatabaseMovie,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "actorId",
        associateBy = Junction(MovieActorCrossRef::class)
    )
    val actors: List<DatabaseActor>
)
