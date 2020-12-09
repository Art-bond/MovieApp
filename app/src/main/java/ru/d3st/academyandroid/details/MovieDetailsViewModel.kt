package ru.d3st.academyandroid.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.domain.Movie

class MovieDetailsViewModel(selectedMovie: Movie) : ViewModel() {

    private val _movieData = MutableLiveData<Movie>()
    val movieData: LiveData<Movie>
        get() = _movieData
    private val movie = selectedMovie

    init {
        executeTask()
    }

    private fun executeTask() = viewModelScope.launch {
        getMovieDetail(movie)
    }

    private fun getMovieDetail(movie: Movie) {
        _movieData.value = movie

    }
}