package ru.d3st.academyandroid.overview

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.domain.*
import ru.d3st.academyandroid.domain.tmdb.ResponseMovieTMDB
import ru.d3st.academyandroid.network.MovieApi
import timber.log.Timber

class MovieListViewModel(application: Application) : AndroidViewModel(application) {


    //Сбор информации для заполнения полей списка групп пользоватея
    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    init {
        viewModelScope.launch {
            onPreExecute()
            val resultList = getPopularMovieProperties()
            val genreList = getGenreList()
            Timber.i("response genres $genreList")
            onPostExecute(resultList,genreList)
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

    private fun onPostExecute(
        resultList: List<ResponseMovieTMDB.MovieNetwork>,
        genres: List<Genre>
    ) {
        val genresMap = genres.associateBy { it.id }

        _movieList.value = resultList.asDomainModel(genresMap)

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

    private suspend fun getPopularMovieProperties(): List<ResponseMovieTMDB.MovieNetwork> =
        withContext(Dispatchers.IO) {
            try {
                val response = MovieApi.retrofitService.getNovPlayingMovie()
                Timber.i("Response  is $response")
                return@withContext response.results
            } catch (e: Exception) {
                Timber.e("Response exception is $e")
                return@withContext ArrayList<ResponseMovieTMDB.MovieNetwork>()
            }
        }




    private fun List<ResponseMovieTMDB.MovieNetwork>.asDomainModel(genresMap: Map<Int, Genre>): List<Movie> {

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
                    genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                },
                actors = ArrayList(),
                votes = movie.voteCount
                )
        }
    }




}


