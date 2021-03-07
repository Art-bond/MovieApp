package ru.d3st.academyandroid.repository

import io.reactivex.Observable
import ru.d3st.academyandroid.domain.ActorBio
import ru.d3st.academyandroid.network.MovieApi
import ru.d3st.academyandroid.network.asDomainModel
import ru.d3st.academyandroid.repository.base.BaseActorBioRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActorBioRepository @Inject constructor(
    private val movieApi: MovieApi,
) : BaseActorBioRepository {

    override fun getActorBio(actorId: Int): Observable<ActorBio> =
        movieApi.networkService.getActorBio(actorId)
            .map { it.asDomainModel() }
            .toObservable()
    }






