package ru.d3st.movieapp.repository.baseRepositories

import ru.d3st.movieapp.domain.ActorBio
import ru.d3st.movieapp.network.Resource

interface BaseActorBioRepository{
    suspend fun getActorBio(actorId: Int): Resource<ActorBio>
}