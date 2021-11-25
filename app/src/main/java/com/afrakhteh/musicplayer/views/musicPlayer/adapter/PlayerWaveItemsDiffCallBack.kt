package com.afrakhteh.musicplayer.views.musicPlayer.adapter

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.wave.WaveItemModel

class PlayerWaveItemsDiffCallBack : DiffUtil.ItemCallback<WaveItemModel>() {
    override fun areItemsTheSame(oldItem: WaveItemModel, newItem: WaveItemModel): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: WaveItemModel, newItem: WaveItemModel): Boolean {
        return oldItem == newItem
    }
}