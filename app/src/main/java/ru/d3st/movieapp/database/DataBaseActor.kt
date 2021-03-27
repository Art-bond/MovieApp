package ru.d3st.movieapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.d3st.movieapp.domain.Actor

@Entity
data class DatabaseActor constructor(
    @PrimaryKey
    val actorId: Int,
    val name: String,
    val picture: String
)

fun List<DatabaseActor>.asDomainModel(): List<Actor> {
    return map { actor ->
        Actor(
            id = actor.actorId,
            name = actor.name,
            picture = actor.picture
        )
    }
}


