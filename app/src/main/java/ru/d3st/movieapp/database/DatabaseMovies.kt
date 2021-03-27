package ru.d3st.movieapp.database


import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.d3st.movieapp.domain.Movie


@Entity
data class DatabaseMovie constructor(
    @PrimaryKey
    val movieId: Int, //ID фильма
    val title: String, //название фильма
    val overview: String, //описание
    val poster: String,
    val backdrop: String,
    val ratings: Float,
    val adult: Boolean,
    val runtime: Int,
    val genres: List<String>,
    val votes: Int,
    var nowPlayed: Boolean

)


/**
 * Map DatabaseMovie to domain entities
 */
fun List<DatabaseMovie>.asDomainModel(): List<Movie> {
    return map {
        Movie(
            id = it.movieId,
            title = it.title,
            overview = it.overview,
            poster = it.poster,
            backdrop = it.backdrop,
            ratings = it.ratings,
            adult = it.adult,
            runtime = it.runtime,
            genres = it.genres,
            votes = it.votes
        )
    }
}




