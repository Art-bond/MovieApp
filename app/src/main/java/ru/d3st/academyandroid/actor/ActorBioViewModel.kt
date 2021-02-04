package ru.d3st.academyandroid.actor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.tmdb.ResponseActorBioContainer
import ru.d3st.academyandroid.network.tmdb.ResponseMovieActorsContainer
import ru.d3st.academyandroid.network.MovieApi
import timber.log.Timber
import java.lang.Exception


class ActorBioViewModel(actor: Actor) : ViewModel() {

    val selectedActor = actor

    private val _actorsMovies = MutableLiveData<List<Movie>>()
    val actorsMovies: LiveData<List<Movie>>
        get() = _actorsMovies

    private val _actorsBio = MutableLiveData<ResponseActorBioContainer>()
    val actorsBio: LiveData<ResponseActorBioContainer>
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
                _actorsBio.value = responseActorBio
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
    private fun List<ResponseMovieActorsContainer.Cast>.asDomainModel(genresMap: Map<Int, Genre>): List<Movie> {

        return map {movie ->
            Movie(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                poster = "https://image.tmdb.org/t/p/w342" + movie.posterPath,
                backdrop = "https://image.tmdb.org/t/p/w342" + movie.backdropPath,
                ratings = movie.voteAverage.toFloat(),
                adult = movie.adult,
                runtime = 0,
                genres = movie.genreIds.map {
                    genresMap[it]?.name ?: throw IllegalArgumentException("Genre not found")
                },
                actors = ArrayList(),
                votes = movie.voteCount
            )
        }
    }

}