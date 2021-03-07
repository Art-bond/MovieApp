package ru.d3st.academyandroid.repository

import androidx.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.d3st.academyandroid.database.DatabaseMovie
import ru.d3st.academyandroid.database.MovieDao
import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.network.*
import ru.d3st.academyandroid.network.tmdb.ResponseMovieContainer
import ru.d3st.academyandroid.repository.base.BaseMovieRepository
import ru.d3st.academyandroid.utils.Status
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MoviesRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val movieApi: MovieApi,
) : BaseMovieRepository {

    private val disposable = CompositeDisposable()
    private val genres = MutableLiveData<Map<Int, Genre>>()
    val movies = MutableLiveData<APIResponse<List<Movie>>>()




    val moviesNowPlayed: Flowable<List<Movie>> = movieDao.getMoviesFlow()
        .map { movies ->
            movies.filter { it.nowPlayed }.asDomainModel()
        }

    override fun getMovieDetail(movieId: Int): Observable<Movie> {
        return movieDao.getMovie(movieId)
            .map { it.asDomainModel() }
    }


    override fun getMoviesByActor(actorId: Int): Observable<List<Movie>> {
        return movieApi.networkService.getActorsMovies(actorId)
            .toObservable()
            .doOnNext {
                movieDao.insertAll(it.asDataBaseModel(genres.value!!))
            }
            .map {
                it.asDomainModel(genres.value!!)
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun refreshMovies() {
        getGenres()
        Timber.d("refresh movies is called")
        //list contains movie that now playing in cinema
        val movieObservable =
            movieApi.networkService.getNovPlayingMovie()

        val apiObservable = movieObservable
            .map {
                it.asDatabaseModelNowPlayed(genres.value)
            }
            .doOnNext { movies ->
                movies.forEach { it.nowPlayed = true }
                movieDao.insertAll(movies)
            }
            .doOnEach { Timber.i("api contained is ${it.value} or ${it.error}") }

        val dbObservable = movieDao.getMovies()
            .doOnEach { Timber.i("db contained is ${it.value} or ${it.error}") }


        observeData(dbObservable, apiObservable)

    }

    private fun updateResponse(
        status: Status,
        data: List<DatabaseMovie>? = null,
        errorMessage: String? = null,
    ) {
        val responseLoading = APIResponse(status, data?.asDomainModel(), errorMessage)
        movies.value = responseLoading
    }

    private fun observeData(
        db: Observable<List<DatabaseMovie>>,
        remote: Observable<List<DatabaseMovie>>,
    ) {
        disposable.add(
            Observable.concat(db, remote)
                .filter { it.isNotEmpty() }
                .timeout(1000, TimeUnit.MILLISECONDS)
                .onErrorResumeNext(remote)
                .firstElement()
                .map { movies ->
                    movies.filter { it.nowPlayed }
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    updateResponse(Status.LOADING)
                }
                .subscribe(
                    {
                        updateResponse(Status.SUCCESS, it)
                    },
                    {
                        Timber.e("observerData error. ${it.localizedMessage}")
                        updateResponse(Status.ERROR, null, it.localizedMessage)
                    }

                ))
    }


    private fun getGenres() {
        disposable.add(
            movieApi.networkService.getGenres()
                .toObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { container ->
                        genres.value = container.genres.associateBy { it.id }
                    },
                    {
                        Timber.e("get genres Error ${it.localizedMessage}")
                        genres.value = emptyMap()
                    }
                ))
    }

    private fun removeNoLongerGoingMovieFromDataBase(newMovieList: List<DatabaseMovie>) {
        val oldMovieList: List<DatabaseMovie> =
            movieDao.getMoviesSync().filter { it.nowPlayed }
        val oldMinusNewIds =
            oldMovieList.map { it.movieId }.asSequence().minus(newMovieList.map { it.movieId })
        val oldMinusNewList = oldMovieList.filter { it.movieId in oldMinusNewIds }
        oldMinusNewList.forEach { it.nowPlayed = false }

        movieDao.updateAll(oldMinusNewList)
    }


    private fun compareNowPlayedMovies(newMovieList: List<DatabaseMovie>): List<DatabaseMovie> {
        val oldMovieList: List<DatabaseMovie> =
            movieDao.getMoviesSync().filter { it.nowPlayed }
        val diffNewMovieIds =
            newMovieList.map { it.movieId }.asSequence().minus(oldMovieList.map { it.movieId })

        val diffList: List<DatabaseMovie> =
            newMovieList.filter { it.movieId in diffNewMovieIds }
        Timber.i(
            "MovieRepository with WorkManager dist list have data ${diffList.size}"
        )
        return diffList
    }
}

















