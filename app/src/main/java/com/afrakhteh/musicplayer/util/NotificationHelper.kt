package com.afrakhteh.musicplayer.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.views.playMusicActivity.PlayerActivity

class NotificationHelper(
        private val context: Context
) {
    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, Strings.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setSound(null)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
    }
    private val contentIntent by lazy {
        PendingIntent.getActivity(
                context,
                0,
                Intent(context, PlayerActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() =
            NotificationChannel(
                    Strings.NOTIFICATION_CHANNEL_ID,
                    Strings.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = Strings.NOTIFICATION_CHANNEL_DESCRIPTION
                setSound(null, null)
            }

    fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    createChannel()
            )
        }
        return notificationBuilder.build()
    }

    fun updateNotification(notificationText: Strings? = null) {
        notificationText?.let {
            notificationBuilder.setContentText(it.toString())
        }
        notificationManager.notify(
                Numerals.NOTIFICATION_ID,
                notificationBuilder.build()
        )
    }


}