package ru.d3st.academyandroid.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.database.Data
import ru.d3st.academyandroid.domain.Movie

class MovieListViewModel : ViewModel() {


        val movies: List<Movie> = listOf(Data.movie1, Data.movie2, Data.movie3, Data.movie4)

    //Сбор информации для заполнения полей списка групп пользоватея
    private var _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    init {
        viewModelScope.launch {
            onPreExecute()
        }
    }

    private fun onPreExecute() {
        // show progress
        _movieList.value = movies
    }

    fun displayMovieDetails(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    fun displaySelectedGroupComplete() {
        _navigateToSelectedMovie.value = null
    }
}