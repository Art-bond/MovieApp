package ru.d3st.academyandroid.repository.base

import io.reactivex.Observable
import ru.d3st.academyandroid.domain.ActorBio

interface BaseActorBioRepository {
    fun getActorBio(actorId: Int): Observable<ActorBio>
}