package com.afrakhteh.musicplayer.views.musicPlayer.adapter

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.WaveModel

class PlayerWaveItemsDiffCallBack : DiffUtil.ItemCallback<WaveModel>() {
    override fun areItemsTheSame(oldItem: WaveModel, newItem: WaveModel): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: WaveModel, newItem: WaveModel): Boolean {
        return oldItem == newItem
    }
}