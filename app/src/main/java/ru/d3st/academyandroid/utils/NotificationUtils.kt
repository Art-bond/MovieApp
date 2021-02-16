package ru.d3st.academyandroid.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import ru.d3st.academyandroid.R

object NotificationUtils {

    private const val NOTIFICATION_ID = 0
    private const val REQUEST_CODE = 0
    private const val FLAGS = 0

    fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.movie_channel)
        )
            .setSmallIcon(R.drawable.ic_target)
            .setContentTitle(applicationContext.getString(R.string.movie_detail))
            .setContentText(messageBody)

        notify(NOTIFICATION_ID, builder.build())
    }

}