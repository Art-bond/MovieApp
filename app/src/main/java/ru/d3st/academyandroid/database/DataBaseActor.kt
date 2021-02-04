package ru.d3st.academyandroid.database

import androidx.room.*

    @Entity
    data class DatabaseActor constructor(
        @PrimaryKey
        val actorId: Int,
        val name: String,
        val picture: String?
    )

    @Entity(primaryKeys = ["movieId", "actorId"])
    class MovieActorCrossRef(
        val movieId: Int,
        val actorId: Int
    )

    data class MovieWithActors(
        @Embedded
        var movie: DatabaseMovie,
        @Relation(
            parentColumn = "movieId",
            entityColumn = "actorId",
            associateBy = Junction(
                value = MovieActorCrossRef::class
            )
        )
        var actors: List<DatabaseActor>
    )
