package ru.d3st.movieapp.work

import android.content.Context
import androidx.core.os.bundleOf
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import ru.d3st.movieapp.R
import ru.d3st.movieapp.database.DatabaseMovie
import ru.d3st.movieapp.domain.Movie
import ru.d3st.movieapp.notification.Notifier
import ru.d3st.movieapp.repository.MoviesRepository
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
    override suspend fun doWork(): Result {
        return try {
            repository.newMovies().collect {

                if (it != emptyList<DatabaseMovie>()) {
                    Timber.d("WorkManager compareList contains ${it.first().title}")
                    notifyBestMovie(it)
                    Timber.d("WorkManager request Success")
                }
            }
            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun notifyBestMovie(movies: List<Movie>) {
        val args = bundleOf("selected_movie" to movies.first().id)
        val movie = movies.first()
        val poster =
            Glide.with(applicationContext)
                .asBitmap()
                .skipMemoryCache(true)
                .load(movie.poster)
                .submit()
                .get()


        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.movieDetailsFragment)
            .setArguments(args)
            .createPendingIntent()
        Notifier.postNotification(
            movie.id,
            movie.title,
            poster,
            applicationContext,
            pendingIntent
        )
    }


}