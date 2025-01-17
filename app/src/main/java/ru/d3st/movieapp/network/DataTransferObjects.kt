package ru.d3st.movieapp.network

import ru.d3st.movieapp.database.DatabaseActor
import ru.d3st.movieapp.database.DatabaseMovie
import ru.d3st.movieapp.database.MovieActorCrossRef
import ru.d3st.movieapp.domain.Actor
import ru.d3st.movieapp.domain.ActorBio
import ru.d3st.movieapp.domain.Genre
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.network.tmdb.ResponseActorBioContainer
import ru.d3st.movieapp.network.tmdb.ResponseActorsContainer
import ru.d3st.movieapp.network.tmdb.ResponseMovieActorsContainer
import ru.d3st.movieapp.network.tmdb.ResponseMovieContainer

const val tmdbBeginString = "https://image.tmdb.org/t/p/w342"

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
            votes = movie.voteCount
        )
    }
}

/**
 * Convert Network results to database objects
 */

fun ResponseMovieContainer.asDatabaseModelNowPlayed(genresMap: Map<Int, Genre>): List<DatabaseMovie> {
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
            votes = movie.voteCount,
            nowPlayed = true
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
    }.take(4)
}

fun ResponseActorsContainer.asMovieActorCross(movieId: Int): List<MovieActorCrossRef> {
    return cast.map { actor ->
        MovieActorCrossRef(
            movieId = movieId,
            actorId = actor.id
        )
    }.take(4)
}


fun ResponseActorsContainer.asDomainActorModel(): List<Actor> {
    return cast.map { cast ->
        Actor(
            id = cast.id,
            name = cast.name,
            picture = tmdbBeginString + cast.profilePath
        )
    }
}

fun List<ResponseMovieActorsContainer.Cast>.asDomainModel(genresMap: Map<Int, Genre>): List<Movie> {

    return map { movie ->
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
            votes = movie.voteCount

        )
    }
}
fun ResponseMovieActorsContainer.asDataBaseModel(genresMap: Map<Int, Genre>): List<DatabaseMovie> {
    return cast.map { movie ->
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
            votes = movie.voteCount,
            nowPlayed = false
        )
    }
}

fun ResponseActorBioContainer.asDomainModel(): ActorBio {
    return ActorBio(
            birthday = birthday,
            knownForDepartment = knownForDepartment,
            deathday= deathday,
            id = id,
            name = name,
            alsoKnownAs= alsoKnownAs,
            gender = gender,
            biography = biography,
            popularity = popularity,
            placeOfBirth = placeOfBirth,
            profilePath = tmdbBeginString + profilePath,
            adult = adult,
            imdbId = imdbId,
            homepage = homepage
        )
    }


