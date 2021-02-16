package ru.d3st.academyandroid.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import ru.d3st.academyandroid.database.getDatabase
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters) : CoroutineWorker(
    appContext,
    params
) {
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = MoviesRepository(database)
        try {
            repository.refreshMovies()
            Timber.d("Work request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }
    companion object{
        const val WORK_NAME = "u.d3st.academyandroid.work.RefreshDataWorker"
    }
}