package ru.d3st.academyandroid.network

import androidx.lifecycle.LiveData
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.network.Resource.*

class ActorManager : LiveData<Resource<List<Actor>?>>() {

    init {
        value = Success(null)
    }

    /**
     * Set the [Actor] value and notifies observers.
     */
    internal fun set(actors: List<Actor>) {
        postValue(Success(actors))
    }

    /**
     * Clear any information from the device.
     */
    internal fun clear() {
        postValue(Success(null))
    }

    /**
     * Signals that the resource information is being retrieved from network.
     */
    internal fun loading() {
        postValue(Loading())
    }

    /**
     * Signals that an error occurred when trying to fetch the resource information.
     */
    internal fun error(t: Throwable) {
        postValue(Failure(t))
    }
}