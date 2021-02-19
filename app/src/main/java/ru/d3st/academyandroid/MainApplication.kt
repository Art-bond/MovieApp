package ru.d3st.academyandroid

import android.app.Application
import android.os.Build
import androidx.core.os.bundleOf
import androidx.hilt.work.HiltWorkerFactory
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.notification.Notifier
import ru.d3st.academyandroid.work.RefreshDataWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.coroutineContext


@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()


    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }
            .build()
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )

    }

    private fun delayedInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
            setupRecurringWork()
        }
    }

    override fun onCreate() {
        super.onCreate()
        delayedInit()
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