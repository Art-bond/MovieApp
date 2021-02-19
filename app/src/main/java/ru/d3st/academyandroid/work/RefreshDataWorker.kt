package ru.d3st.academyandroid.work

import android.content.Context
import androidx.core.os.bundleOf
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.d3st.academyandroid.R
import ru.d3st.academyandroid.database.DatabaseMovie
import ru.d3st.academyandroid.database.asDomainModel
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.notification.Notifier
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: MoviesRepository
) : CoroutineWorker(
    appContext,
    params
) {
    companion object {
        const val WORK_NAME = "ru.d3st.academyandroid.work.RefreshDataWorker"
    }

    override suspend fun doWork(): Result {

        try {
            Timber.d("WorkManager request for sync is run")
            val movies = repository.refreshMovies()
            if (movies != emptyList<DatabaseMovie>()) {
                Timber.d("WorkManager compareList contains ${movies.map { it.title }}")
                notifyBestMovie(movies.asDomainModel())
            }
            Timber.d("WorkManager request Success")
            return Result.success()

        } catch (e: Exception) {
            Timber.e("WorkManager error $e")
            return Result.retry()
        }
    }

    private fun notifyBestMovie(movies: List<Movie>) {
        val args = bundleOf("selected_movie" to movies.first().id)
        val movie = movies.first()

        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.movieDetailsFragment)
            .setArguments(args)
            .createPendingIntent()
        Notifier.postNotification(movie.id, movie.title, applicationContext, pendingIntent)
    }

}