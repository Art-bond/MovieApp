package ru.d3st.academyandroid.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.domain.Event
import ru.d3st.academyandroid.storage.EventDao

class DialogViewModel(private val eventDao: EventDao) : ViewModel() {

    private var eventLiveData: LiveData<Event>? = null

    fun get(id: Long): LiveData<Event> {
        return eventLiveData ?: liveData {
            emit(eventDao.get(id))
        }.also {
            eventLiveData = it
        }
    }

    fun addData(
        id: Long,
        date: String,
        name: String,

    ) {
        val event = Event(id, date, name)

        viewModelScope.launch {
            if (id > 0) {
                update(event)
            } else {
                insert(event)
            }
        }
    }

    private suspend fun insert(event: Event): Long {
        return eventDao.insert(event)
    }

    private suspend fun update(event: Event) {
        return eventDao.update(event)

    }

}