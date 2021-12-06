package com.afrakhteh.musicplayer.views.main.adapters.liked

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay

class LikedDiffCallBack : DiffUtil.ItemCallback<AudioPrePareToPlay>() {
    override fun areItemsTheSame(oldItem: AudioPrePareToPlay, newItem: AudioPrePareToPlay): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AudioPrePareToPlay, newItem: AudioPrePareToPlay): Boolean {
        return newItem == oldItem
    }
}