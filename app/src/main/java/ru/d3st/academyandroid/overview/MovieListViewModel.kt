package ru.d3st.academyandroid.overview

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.database.getDatabase
import ru.d3st.academyandroid.domain.*
import ru.d3st.academyandroid.network.MovieApi
import ru.d3st.academyandroid.network.asDomainModel
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber
import java.io.IOException

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val moviesRepository = MoviesRepository(getDatabase(application))

    val playlist = moviesRepository.movies

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

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
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    /**
     * A playlist of videos displayed on the screen.
     */
    private val _movieList = playlist
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    init {
        refreshDataFromRepository()
        viewModelScope.launch {
            onPreExecute()

            val genreList = getGenreList()

            Timber.i("response genres $genreList")
            //val movies = loadMovies(getApplication())

        }
    }

    private suspend fun getGenreList(): List<Genre> =
        withContext(Dispatchers.IO) {
            try {
                val response = MovieApi.retrofitService.getGenres()
                Timber.i("Response genres is $response")
                return@withContext response.genres
            } catch (e: Exception) {
                Timber.e("Response genres exception is $e")

                return@withContext ArrayList()
            }
        }

    private fun onPostExecute() {


    }

    private fun onPreExecute() {
        // show progress

    }

    fun displayMovieDetailsBegin(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    fun displaySelectedMovieComplete() {
        _navigateToSelectedMovie.value = null
    }

/*    private suspend fun getPopularMovieProperties(genres : List<Genre>): List<Movie> =
        withContext(Dispatchers.IO) {
            try {
                val genresMap: Map<Int, Genre> = genres.associateBy { it.id }
                val response = MovieApi.retrofitService.getNovPlayingMovie()
                Timber.i("Response  is $response")
                return@withContext response.asDomainModel(genresMap)
            } catch (e: Exception) {
                Timber.e("Response exception is ${e.localizedMessage}")
                return@withContext ArrayList<Movie>()
            }
        }*/

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    private fun refreshDataFromRepository(){
        viewModelScope.launch {
            try {
                moviesRepository.refreshMovies()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(playlist.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }





}


