package ru.d3st.academyandroid.repository

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.database.MovieWithActors
import ru.d3st.academyandroid.database.MovieWithActorsDao
import ru.d3st.academyandroid.database.MoviesDataBase
import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.network.*
import ru.d3st.academyandroid.network.tmdb.ResponseActorsContainer
import ru.d3st.academyandroid.repository.base.BaseActorRepository
import ru.d3st.academyandroid.utils.Status
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ActorsRepository @Inject constructor(
    private val actorsDao: MovieWithActorsDao,
) : BaseActorRepository {
    private val disposable = CompositeDisposable()
    override val actors = MutableLiveData<APIResponse<List<Actor>>>()

    override fun fetchActorsInMovie(movieId: Int) {
        Timber.i("Response actor is called")
        val apiActor =
            MovieApi.networkService.getActors(movieId)
                .toObservable()
                .doOnNext {
                    actorsDao.insertMovieWithActors(
                        actors = it.asDataBaseActorModel(),
                        movieWithActors = it.asMovieActorCross(movieId))
                }
                .map {
                    it.asDomainActorModel()
                }

        val databaseActor =
            actorsDao.getActorsTheMovie(movieId)
                .map{
                    it.actors.asDomainModel()
                }


        observeData(databaseActor, apiActor)
    }

    private fun observeData(
        db: Observable<List<Actor>>,
        remote: Observable<List<Actor>>,
    ) {
        disposable.add(
            Observable.concat(db, remote)
                .filter {
                    it.isNotEmpty()
                }
                .timeout(1000, TimeUnit.MILLISECONDS)
                .onErrorResumeNext(remote)
                .firstElement()
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
                        updateResponse(Status.ERROR, null, it.localizedMessage)
                    }
                ))
    }

    private fun updateResponse(status: Status, data: List<Actor>? = null, errorMessage: String? = null) {
        val responseLoading = APIResponse(status, data, errorMessage)
        actors.value = responseLoading
    }

}
