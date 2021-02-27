package ru.d3st.academyandroid.repository

import kotlinx.coroutines.Dispatchers
import ru.d3st.academyandroid.domain.ActorBio
import ru.d3st.academyandroid.network.MovieApi
import ru.d3st.academyandroid.network.Resource
import ru.d3st.academyandroid.network.asDomainModel
import ru.d3st.academyandroid.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorBioRepository @Inject constructor(
    private val movieApi: MovieApi,
):BaseRepository {
    override suspend fun getActorBio(actorId: Int): Resource<ActorBio> {
        return safeApiCall(Dispatchers.IO){
            movieApi.networkService.getActorBio(actorId).asDomainModel()
        }
    }

}
interface BaseRepository{
    suspend fun getActorBio(actorId: Int):Resource<ActorBio>
}

