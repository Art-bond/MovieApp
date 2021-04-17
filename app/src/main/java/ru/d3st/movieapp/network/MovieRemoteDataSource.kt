package ru.d3st.movieapp.network

import kotlinx.coroutines.Dispatchers
import ru.d3st.movieapp.database.DatabaseMovie
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val movieApi: MovieApi
) {

    suspend fun getMoviesNovPlay(): Resource<List<DatabaseMovie>> {
        return safeApiCall(Dispatchers.IO){
            movieApi.networkService.getNovPlayingMovie().asDatabaseModelNowPlayed(getGenres())
        }
    }

    suspend fun getActorsMovies(actorId: Int): Resource<List<DatabaseMovie>> {
        return safeApiCall(Dispatchers.IO) {
            movieApi.networkService.getActorsMovies(actorId).asDataBaseModel(getGenres())
        }
    }

    private suspend fun getGenres() =
        movieApi.networkService.getGenres().genres.associateBy { it.id }
}