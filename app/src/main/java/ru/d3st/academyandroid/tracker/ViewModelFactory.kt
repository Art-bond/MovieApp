package ru.d3st.academyandroid.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.d3st.academyandroid.dialog.DialogViewModel
import ru.d3st.academyandroid.storage.EventDao

class ViewModelFactory(private  val eventDao: EventDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventListViewModel(eventDao) as T
        } else if (modelClass.isAssignableFrom(DialogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DialogViewModel(eventDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
