package com.afrakhteh.musicplayer.views.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.AudioActions
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.model.entity.AudioPrePareToPlay
import com.afrakhteh.musicplayer.util.resize
import com.afrakhteh.musicplayer.views.musicPlayer.PlayerActivity
import com.afrakhteh.musicplayer.views.services.AudioPlayerService

class PlayerNotificationHelper(
    private val notificationManager: NotificationManager,
    private val resources: Resources
) {
    fun showNotification(
            context: Context,
            audioList: List<AudioPrePareToPlay>,
            position: Int,
            isPlaying: Boolean,
            albumArt: Bitmap?
    ): Notification {
        val builder = getBasePlayerNotificationBuilder(context, audioList, position)
        builder.apply {
            addAction(getNotificationAction(context, AudioActions.ACTION_PREVIOUS))
            addAction(getNotificationAction(context, AudioActions.ACTION_PLAY, isPlaying))
            addAction(getNotificationAction(context, AudioActions.ACTION_NEXT))

            setContentTitle(audioList[position].musicName)
            setContentText(audioList[position].musicArtist)

            setLargeIcon(albumArt ?: getPlaceHolder())

            setOngoing(isPlaying)

        }

        getNotificationChannel()?.let { notificationChannel ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        notificationManager.notify(Numerals.NOTIFICATION_ID, builder.build())
        return builder.build()
    }

    private fun getBasePlayerNotificationBuilder(
            context: Context,
            audioList: List<AudioPrePareToPlay>,
            position: Int
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, Strings.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_play)
                .setSound(null)
                .setVibrate(null)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getContentIntent(context, audioList, position))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(getMediaStyle(context))
    }

    private fun getMediaStyle(
            context: Context
    ): MediaStyle {
        val mediaSessionCompat = MediaSessionCompat(context, Strings.MEDIA_SESSION_TAG)
        return MediaStyle().setMediaSession(mediaSessionCompat.sessionToken)
    }

    private fun getContentIntent(
            context: Context,
            audioList: List<AudioPrePareToPlay>,
            position: Int
    ): PendingIntent {
        return PendingIntent.getActivity(
                context,
                0,
                Intent(context, PlayerActivity::class.java).apply {
                    flags = FLAG_ACTIVITY_SINGLE_TOP
                    putParcelableArrayListExtra(Strings.AUDIO_All_MUSIC_LIST_KEY, ArrayList(audioList))
                    putExtra(Strings.AUDIO_ACTIVE_POSITION__KEY, position)
                },
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getMusicPlayerActionIntent(
            context: Context,
            action: String
    ): PendingIntent {
        val intent = Intent().apply {
            this.action = action
            putExtra(Strings.SERVICE_ACTIONS, action)
            component = ComponentName(context, AudioPlayerService::class.java)
        }
        val flags = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getService(context, 0, intent, flags)
    }

    private fun getNotificationAction(
            context: Context,
            action: String,
            isPlaying: Boolean = false
    ): NotificationCompat.Action {
        val icon = when (action) {
            AudioActions.ACTION_NEXT -> R.drawable.ic_skip_next
            AudioActions.ACTION_PREVIOUS -> R.drawable.ic_skip_previous
            else -> if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        }
        return NotificationCompat.Action.Builder(
                icon, "", getMusicPlayerActionIntent(context, action)).build()
    }

    private fun getNotificationChannel(): NotificationChannel? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return null
        }
        return NotificationChannel(
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
    }

    private fun getPlaceHolder(): Bitmap {
        return BitmapFactory.decodeResource(resources, R.drawable.dog).resize()
    }

    fun cancelNotification() {
        notificationManager.cancel(Numerals.NOTIFICATION_ID)
    }
}