package ru.d3st.academyandroid.actor

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.database.getDatabase
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.domain.ActorBio
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.tmdb.ResponseActorBioContainer
import ru.d3st.academyandroid.network.tmdb.ResponseMovieActorsContainer
import ru.d3st.academyandroid.network.MovieApi
import ru.d3st.academyandroid.network.asDomainModel
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber
import java.lang.Exception


class ActorBioViewModel(actor: Actor, application: Application) : ViewModel() {


    private val moviesRepository = MoviesRepository(getDatabase(application))


    private val _actorsMovies = MutableLiveData<List<Movie>>()
    val actorsMovies: LiveData<List<Movie>>
        get() = _actorsMovies

    private val _actorsBio = MutableLiveData<ActorBio>()
    val actorsBio: LiveData<ActorBio>
        get() = _actorsBio

    private val _navigateToMovieDetails = MutableLiveData<Movie>()
    val navigateToMovieDetails: LiveData<Movie>
        get() = _navigateToMovieDetails

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        getActorsMovieData(actor)
        getActorBioData(actor)
    }

    private fun getActorsMovieData(actor: Actor) {
        viewModelScope.launch {
            try {
                val responseActorsMovies = MovieApi.retrofitService.getActorsMovies(actor.id)
                moviesRepository.refreshActorsBioMovie(actor.id)
                val responseGenreList = MovieApi.retrofitService.getGenres()
                Timber.i("ActorsMovies. actor's movie list has been loaded $responseActorsMovies")
                val genresMap = responseGenreList.genres
                    val genres = genresMap.associateBy { it.id }
                _actorsMovies.value = responseActorsMovies.cast.asDomainModel(genres)
                Timber.i("ActorsMovies. actors movie list contains ${_actorsMovies.value!!.size} movies")
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (e: Exception) {
                Timber.e("ActorsMovies. error is $e")
                if (actorsMovies.value.isNullOrEmpty())
                    _eventNetworkError.value = true

            }
        }
    }

    private fun getActorBioData(actor: Actor) {
        viewModelScope.launch {
            try {
                val responseActorBio = MovieApi.retrofitService.getActorBio(actor.id)
                _actorsBio.value = responseActorBio.asDomainModel()
            } catch (e: Exception) {

            }
        }
    }

    fun onMovieSelected(movie : Movie) {
        _navigateToMovieDetails.value = movie
    }

    fun onMovieNavigated() {
        _navigateToMovieDetails.value = null
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }


}



