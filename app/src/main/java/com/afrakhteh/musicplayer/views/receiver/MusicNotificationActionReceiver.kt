package com.afrakhteh.musicplayer.views.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.afrakhteh.musicplayer.constant.AudioActions
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.views.services.AudioPlayerService

class MusicNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val actionIntent = Intent(
                context,
                AudioPlayerService::class.java)

        when (requireNotNull(intent).action) {
            AudioActions.ACTION_PLAY -> {
                Toast.makeText(context, "play", Toast.LENGTH_LONG).show()
                actionIntent.putExtra(Strings.SERVICE_ACTIONS, intent.action)
                requireNotNull(context).startService(actionIntent)
            }
            AudioActions.ACTION_PREVIOUS -> {
                Toast.makeText(context, "ACTION_PREVIOUS", Toast.LENGTH_LONG).show()
                actionIntent.putExtra(Strings.SERVICE_ACTIONS, intent.action)
                requireNotNull(context).startService(actionIntent)
            }
            AudioActions.ACTION_NEXT -> {
                Toast.makeText(context, "ACTION_NEXT", Toast.LENGTH_LONG).show()
                actionIntent.putExtra(Strings.SERVICE_ACTIONS, intent.action)
                requireNotNull(context).startService(actionIntent)
            }
        }
    }
}