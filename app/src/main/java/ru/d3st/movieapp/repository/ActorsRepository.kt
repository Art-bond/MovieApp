package ru.d3st.movieapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.d3st.movieapp.database.MoviesDataBase
import ru.d3st.movieapp.database.asDomainModel
import ru.d3st.movieapp.domain.Actor
import ru.d3st.movieapp.network.*
import ru.d3st.movieapp.network.tmdb.ResponseActorsContainer
import ru.d3st.movieapp.repository.baseRepositories.BaseActorsRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorsRepository @Inject constructor(
    private val dataBase: MoviesDataBase
) : BaseActorsRepository {

    override suspend fun refreshMovieWithActors(movieId: Int) {
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

    override suspend fun getActors(movieId: Int): List<Actor> = withContext(Dispatchers.IO) {
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