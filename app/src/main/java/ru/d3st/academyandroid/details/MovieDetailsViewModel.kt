package ru.d3st.academyandroid.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.d3st.academyandroid.domain.Movie

class MovieDetailsViewModel(selectedMovie: Movie) : ViewModel() {

    val movieName = selectedMovie.titleMovie


    private val _movieData = MutableLiveData<Movie>()
    val movieData: LiveData<Movie>
        get() = _movieData

    init {
        setGroupData(selectedMovie)
    }

    private fun setGroupData(selectedMovie: Movie) {
        _movieData.value = selectedMovie

    }
}