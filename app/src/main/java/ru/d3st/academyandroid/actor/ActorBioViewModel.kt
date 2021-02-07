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

    val selectedActor = actor

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
            } catch (e: Exception) {
                Timber.e("ActorsMovies. error is $e")

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


}



