package ru.d3st.academyandroid.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.database.MoviesDataBase
import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.network.MovieApi
import ru.d3st.academyandroid.network.asDataBaseActorModel
import ru.d3st.academyandroid.network.asMovieActorCross
import ru.d3st.academyandroid.network.tmdb.ResponseActorsContainer
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorsRepository @Inject constructor(private val dataBase: MoviesDataBase) {

    suspend fun refreshMovieWithActors(movieId: Int) {
        withContext(Dispatchers.IO) {
            Timber.i("Response actor is called")
            val actors: ResponseActorsContainer = try {
                MovieApi.retrofitService.getActors(movieId)
            } catch (e: Exception) {
                Timber.i("exception on response Actor List")
                ResponseActorsContainer(0, ArrayList())
            }
            Timber.i("actors list contained ${actors.asDataBaseActorModel().size}")

            dataBase.actorDao().insertMovieWithActors(
                actors = actors.asDataBaseActorModel(),
                movieWithActors = actors.asMovieActorCross(movieId)
            )
        }
    }

    suspend fun actors(movieId: Int): List<Actor> = withContext(Dispatchers.IO) {
        return@withContext dataBase.actorDao().getActorsTheMovie(movieId).actors.asDomainModel()

    }
}