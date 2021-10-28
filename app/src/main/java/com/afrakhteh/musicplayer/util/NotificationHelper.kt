package com.afrakhteh.musicplayer.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.AudioActions
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.views.playMusicActivity.PlayerActivity
import com.afrakhteh.musicplayer.views.playMusicActivity.state.AudioState
import com.afrakhteh.musicplayer.views.receiver.MusicNotificationActionReceiver
import com.afrakhteh.musicplayer.views.services.AudioPlayerService

class NotificationHelper(
        private val context: Context
) {
    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val mediaSessionCompat = MediaSessionCompat(context, "player")

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, Strings.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_play)
                .setSound(null)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(contentIntent)
                .setStyle(
                        androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSessionCompat.sessionToken)
                )
                .addAction(getNotificationAction(AudioActions.ACTION_PREVIOUS))
                .addAction(getNotificationAction(AudioActions.ACTION_PLAY))
                .addAction(getNotificationAction(AudioActions.ACTION_NEXT))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

    }
    private val contentIntent by lazy {
        PendingIntent.getActivity(
                context,
                0,
                Intent(context, PlayerActivity::class.java).setFlags(FLAG_ACTIVITY_SINGLE_TOP),
                0
        )
    }

    private fun getMusicPlayerAction(action: String): PendingIntent {
        val intent = Intent().apply {
            this.action = action
            component = ComponentName(context, MusicNotificationActionReceiver::class.java)
        }
        val flags = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getBroadcast(context, 0, intent, flags)
    }

    private fun getNotificationAction(action: String): NotificationCompat.Action {
        var icon = if (AudioPlayerService().state != AudioState.PAUSE_STATE) {
            R.drawable.ic_pause
        } else {
            R.drawable.ic_play
        }
        when (action) {
            AudioActions.ACTION_NEXT -> icon = R.drawable.ic_skip_next
            AudioActions.ACTION_PREVIOUS -> icon = R.drawable.ic_skip_previous
        }
        return NotificationCompat.Action.Builder(
                icon, "", getMusicPlayerAction(action)).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() =
            NotificationChannel(
                    Strings.NOTIFICATION_CHANNEL_ID,
                    Strings.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = Strings.NOTIFICATION_CHANNEL_DESCRIPTION
                setSound(null, null)
                enableLights(false)
                enableVibration(false)
                setShowBadge(false)
            }

    fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    createChannel()
            )
        } else {
            notificationBuilder.priority = NotificationCompat.PRIORITY_LOW
        }
        return notificationBuilder.build()
    }

    fun updateNotification(notificationText: Strings?, notificationTitle: String?) {
        notificationTitle?.let {
            notificationBuilder.setContentTitle(it)
        }
        notificationText?.let {
            notificationBuilder.setContentText(it.toString())
        }
        notificationManager.notify(
                Numerals.NOTIFICATION_ID,
                notificationBuilder.build()
        )
    }

    fun cancelNotification() {
        with(NotificationManagerCompat.from(AudioPlayerService())) {
            cancel(Numerals.NOTIFICATION_ID)
        }
    }
}