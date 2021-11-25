package com.afrakhteh.musicplayer.views.musicPlayer.adapter

import android.view.View
import android.view.ViewGroup
import com.afrakhteh.musicplayer.model.entity.wave.WaveItemModel
import com.afrakhteh.musicplayer.util.toPx

class PlayerEmptyWaveItemViewHolder(view: View) : PlayerViewHolder(view) {

    companion object {
        fun create(screenHeight: Int, parent: ViewGroup): PlayerViewHolder {
            val halfScreen = (screenHeight - 116.toPx) / 2
            return PlayerEmptyWaveItemViewHolder(View(parent.context).apply {
                minimumHeight = halfScreen
            })
        }
    }

    override fun bind(data: WaveItemModel) {}


}