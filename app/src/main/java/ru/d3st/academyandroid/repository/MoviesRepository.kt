package ru.d3st.academyandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.database.MoviesDataBase
import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.MovieApi
import ru.d3st.academyandroid.network.asDatabaseModel
import ru.d3st.academyandroid.network.tmdb.ResponseMovieContainer
import timber.log.Timber

class MoviesRepository(private val dataBase: MoviesDataBase) {



    val movies: LiveData<List<Movie>> = Transformations.map(dataBase.movieDao.getMovies()) {
        it.asDomainModel()
    }


   suspend fun refreshMovies(){
       withContext(Dispatchers.IO){
           Timber.d("refresh movies is called")
           val genres: Map<Int, Genre> = MovieApi.retrofitService.getGenres().genres.associateBy { it.id }
           val movieList: ResponseMovieContainer
           movieList = try {
               val api = MovieApi.retrofitService
               Timber.i("the $api")
               MovieApi.retrofitService.getNovPlayingMovie()
           } catch (e: Exception){
               Timber.i(e)
               ResponseMovieContainer(0,ArrayList(),1,0)
           }

           Timber.i("movie list have data $movieList")
           dataBase.movieDao.insertAll(movieList.asDatabaseModel(genres))
       }
    }




}






