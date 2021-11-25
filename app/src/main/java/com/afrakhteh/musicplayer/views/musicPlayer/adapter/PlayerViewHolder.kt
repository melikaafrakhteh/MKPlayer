package com.afrakhteh.musicplayer.views.musicPlayer.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.model.entity.wave.WaveItemModel

abstract class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(data: WaveItemModel)
}