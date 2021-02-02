package ru.d3st.academyandroid.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.tmdb.ResponseActorsContainer
import ru.d3st.academyandroid.network.MovieApi
import timber.log.Timber

class MovieDetailsViewModel(selectedMovie: Movie) : ViewModel() {

    private val movie = selectedMovie


    private val _movies = MutableLiveData<Movie>()
    val movies: LiveData<Movie>
        get() = _movies

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>>
        get() = _actors


    init {
        fillMovieDetail(selectedMovie)
        executeTask()
    }

    private fun executeTask() =
        viewModelScope.launch {
            onPreExecute() //операции до фонового получения данных
            val result = getActorList()
            onPostExecute(result)
        }

    private fun onPostExecute(result: List<ResponseActorsContainer.Cast>) {
        _actors.value = result.asDomainModel()

    }

    private fun onPreExecute() {
    }


    private fun postExecute() {
    }

    private fun fillMovieDetail(movie: Movie) {
        _movies.value = movie
        //_movieData.value?.actors = actors.asDomainModel()
    }

    private suspend fun getActorList(): List<ResponseActorsContainer.Cast> =
        withContext(Dispatchers.IO) {
            try {
                Timber.i("waiting response with ${movie.id}")
                val response = MovieApi.retrofitService.getActors(movie.id)
                Timber.i("Response actors is $response")
                return@withContext response.cast
            } catch (e: Exception) {
                Timber.e("Response actors exception is $e")

                return@withContext ArrayList()
            }
        }

    private fun List<ResponseActorsContainer.Cast>.asDomainModel(): List<Actor> {
        return map { actor ->
            Actor(
                id = actor.id,
                name = actor.name,
                picture = "https://image.tmdb.org/t/p/w342" + actor.profilePath
            )
        }
    }


}