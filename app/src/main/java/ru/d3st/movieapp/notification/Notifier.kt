package ru.d3st.movieapp.notification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.d3st.movieapp.R

object Notifier {


    private const val channelId = "Default"

    fun init(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                activity.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            val existingChannel = notificationManager.getNotificationChannel(channelId)
            if (existingChannel == null) {
                // Create the NotificationChannel
                val name = activity.getString(R.string.movie_channel)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel(channelId, name, importance)
                mChannel.description = activity.getString(R.string.notificationDescription)
                notificationManager.createNotificationChannel(mChannel)
            }
        }
    }

    fun postNotification(
        id: Int,
        movieTitle: String,
        poster: Bitmap,
        context: Context,
        intent: PendingIntent
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
        builder.setContentTitle(context.getString(R.string.now_playing_movie))
            .setSmallIcon(R.drawable.ic_like)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_like
                )
            )
        val notification = builder
            .setContentText(movieTitle)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(poster)
                    .bigLargeIcon(null)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()
        val notificationManager = NotificationManagerCompat.from(context)
        // Remove prior notifications; only allow one at a time to edit the latest item
        notificationManager.cancelAll()
        notificationManager.notify(id, notification)
    }
}