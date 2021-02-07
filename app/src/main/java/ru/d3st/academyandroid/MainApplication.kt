package ru.d3st.academyandroid

import android.app.Application
import android.os.Build
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.d3st.academyandroid.work.RefreshDataWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainApplication: Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)


    private fun setupRecurringWork(){
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


        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
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
}