package ru.d3st.academyandroid.repository.base

import androidx.lifecycle.MutableLiveData
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.network.APIResponse

interface BaseActorRepository {
    val actors: MutableLiveData<APIResponse<List<Actor>>>
    fun fetchActorsInMovie(movieId: Int)
}