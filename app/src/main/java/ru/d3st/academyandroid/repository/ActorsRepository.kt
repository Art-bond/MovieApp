package ru.d3st.academyandroid.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.database.MoviesDataBase
import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.network.*
import ru.d3st.academyandroid.network.tmdb.ResponseActorsContainer
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorsRepository @Inject constructor(
    private val dataBase: MoviesDataBase
) {

    suspend fun refreshMovieWithActors(movieId: Int) {
        withContext(Dispatchers.IO) {
            Timber.i("Response actor is called")
            val actors: ResponseActorsContainer = try {
                MovieApi.networkService.getActors(movieId)
            } catch (t: Throwable) {
                Timber.i("exception on response Actor List")
                ResponseActorsContainer(0, ArrayList())
            }
            Timber.i("actors list contained ${actors.asDataBaseActorModel().size}")
            if (actors.cast != emptyList<ResponseActorsContainer.Cast>()) {
                dataBase.actorDao().insertMovieWithActors(
                    actors = actors.asDataBaseActorModel(),
                    movieWithActors = actors.asMovieActorCross(movieId)
                )
            }
        }
    }

    suspend fun getActors(movieId: Int): List<Actor> = withContext(Dispatchers.IO) {
        try {
            val actorsFromDataBase = dataBase.actorDao().getActorsTheMovie(movieId).actors
            if (actorsFromDataBase.isNotEmpty()) {
                return@withContext actorsFromDataBase.asDomainModel()
            }
            refreshMovieWithActors(movieId)
            return@withContext dataBase.actorDao().getActorsTheMovie(movieId).actors.asDomainModel()

        } catch (e: Exception) {
            return@withContext emptyList()
        }
    }
}