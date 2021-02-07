package ru.d3st.academyandroid.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.HttpException
import ru.d3st.academyandroid.database.getDatabase
import ru.d3st.academyandroid.repository.MoviesRepository
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(
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