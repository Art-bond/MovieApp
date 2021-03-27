package ru.d3st.movieapp.repository

import kotlinx.coroutines.Dispatchers
import ru.d3st.movieapp.domain.ActorBio
import ru.d3st.movieapp.network.MovieApi
import ru.d3st.movieapp.network.Resource
import ru.d3st.movieapp.network.asDomainModel
import ru.d3st.movieapp.network.safeApiCall
import ru.d3st.movieapp.repository.baseRepositories.BaseActorBioRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorBioRepository @Inject constructor(
    private val movieApi: MovieApi,
): BaseActorBioRepository {
    override suspend fun getActorBio(actorId: Int): Resource<ActorBio> {
        return safeApiCall(Dispatchers.IO){
            movieApi.networkService.getActorBio(actorId).asDomainModel()
        }
    }

}



