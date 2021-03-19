package ru.d3st.academyandroid.repository.baseRepositories

import ru.d3st.academyandroid.domain.ActorBio
import ru.d3st.academyandroid.network.Resource

interface BaseActorBioRepository{
    suspend fun getActorBio(actorId: Int): Resource<ActorBio>
}