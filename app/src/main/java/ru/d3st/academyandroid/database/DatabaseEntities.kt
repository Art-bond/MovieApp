package ru.d3st.academyandroid.database

import androidx.room.*
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.domain.Movie


@Entity
data class DatabaseMovie constructor(
    @PrimaryKey
    val id: Int, //ID фильма
    val title: String, //название фильма
    val overview: String, //описание
    val poster: String,
    val backdrop: String,
    val ratings: Float,
    val adult: Boolean,
    val runtime: Int,
    val genres: List<String>,
    var actors: List<String>,
    val votes: Int

)

@Entity
data class DatabaseGenre(
    @PrimaryKey
    val id: Int, // 28
    val name: String // Action
)
@Entity
data class DatabaseActor(

    val id: Int,
    val name: String,
    val picture: String
)

@Entity
data class DatabaseActorList(
    @PrimaryKey
    val actorListId: Int, // 550
)

@Entity(primaryKeys = ["movieId", "actorId"])
data class ActorListCrossRef(
 val movieId: Int,
 val actorId: Int
)


/**
 * Map DatabaseMovie to domain entities
 */
fun List<DatabaseMovie>.asDomainModel(): List<Movie> {
    return map {
        Movie(
            id = it.id,
            title = it.title,
            overview = it.overview,
            poster = it.poster,
            backdrop = it.backdrop,
            ratings = it.ratings,
            adult = it.adult,
            runtime = it.runtime,
            genres = it.genres,
            actors = it.actors,
            votes = it.votes
        )
    }
}
