package ru.d3st.academyandroid.overview

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.service.loadMovies
import ru.d3st.academyandroid.service.parseMovies

class MovieListViewModel(application: Application) : AndroidViewModel(application) {



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
           val movies = loadMovies(getApplication())
            _movieList.value = movies

        }
    }

    private fun onPreExecute() {
        // show progress

    }

    fun displayMovieDetails(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    fun displaySelectedGroupComplete() {
        _navigateToSelectedMovie.value = null
    }
}