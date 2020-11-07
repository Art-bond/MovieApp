package ru.d3st.academyandroid.tracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.domain.Event
import ru.d3st.academyandroid.storage.EventDao

class EventListViewModel(private val eventDao: EventDao) : ViewModel() {

    val events: LiveData<List<Event>> = eventDao.getAll()

    fun delete(event: Event) = viewModelScope.launch {
        eventDao.delete(event)
    }
}