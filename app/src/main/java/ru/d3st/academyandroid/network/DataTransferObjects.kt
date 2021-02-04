package ru.d3st.academyandroid.network

import ru.d3st.academyandroid.database.DatabaseActor
import ru.d3st.academyandroid.database.DatabaseMovie
import ru.d3st.academyandroid.database.MovieActorCrossRef
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.tmdb.ResponseActorsContainer
import ru.d3st.academyandroid.network.tmdb.ResponseMovieContainer

val tmdbBeginString = "https://image.tmdb.org/t/p/w342"

/**
 * Convert Network results to domain objects
 */

fun ResponseMovieContainer.asDomainModel(genresMap: Map<Int, Genre>): List<Movie> {
    return movies.map { movie ->
        Movie(
            id = movie.id,
            title = movie.title,
            overview = movie.overview,
            poster = tmdbBeginString + movie.posterPath,
            backdrop = tmdbBeginString + movie.backdropPath,
            ratings = movie.voteAverage.toFloat(),
            adult = movie.adult,
            runtime = 0,
            genres = movie.genreIds.map {
                genresMap[it]?.name ?: throw IllegalArgumentException("Genre not found")
            },
            actors = ArrayList(),
            votes = movie.voteCount
        )
    }
}

/**
 * Convert Network results to database objects
 */

fun ResponseMovieContainer.asDatabaseModel(genresMap: Map<Int, Genre>): List<DatabaseMovie> {
    return movies.map { movie ->
        DatabaseMovie(
            movieId = movie.id,
            title = movie.title,
            overview = movie.overview,
            poster = tmdbBeginString + movie.posterPath,
            backdrop = tmdbBeginString + movie.backdropPath,
            ratings = movie.voteAverage.toFloat(),
            adult = movie.adult,
            runtime = 0,
            genres = movie.genreIds.map {
                genresMap[it]?.name ?: throw IllegalArgumentException("Genre not found")
            },
            actors = ArrayList(),
            votes = movie.voteCount
        )
    }
}

fun ResponseActorsContainer.asDataBaseMovieWithActorModel(): List<MovieActorCrossRef> {
    return cast.map { cast ->
        MovieActorCrossRef(
            movieId = id,
            actorId = cast.id
        )
    }
}

fun ResponseActorsContainer.asDataBaseActorModel(): List<DatabaseActor> {
    return cast.map { cast ->
        DatabaseActor(
            actorId = cast.id,
            name = cast.name,
            picture = tmdbBeginString + cast.profilePath
        )
    }
}



