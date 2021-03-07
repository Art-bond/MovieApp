package ru.d3st.academyandroid.network

import ru.d3st.academyandroid.database.DatabaseActor
import ru.d3st.academyandroid.database.DatabaseMovie
import ru.d3st.academyandroid.database.MovieActorCrossRef
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.domain.ActorBio
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.tmdb.*

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

fun ResponseMovieActorsContainer.asDomainModel(genresMap: Map<Int, Genre>):List<Movie> {
    return cast.map {movie ->
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

fun ResponseMovieContainer.asDatabaseModelNowPlayed(genresMap: Map<Int, Genre>?): List<DatabaseMovie> {
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
                genresMap?.get(it)?.name ?: throw IllegalArgumentException("Genre not found")
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


