package ru.d3st.academyandroid.actor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.d3st.academyandroid.domain.Actor

class ActorBioViewModelFactory(
    private val actor: Actor
    ):ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActorBioViewModel::class.java)) {
            return ActorBioViewModel(actor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}