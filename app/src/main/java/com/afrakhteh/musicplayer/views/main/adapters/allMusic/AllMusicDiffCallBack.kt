package com.afrakhteh.musicplayer.views.main.adapters.allMusic

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity

class AllMusicDiffCallBack : DiffUtil.ItemCallback<MusicEntity>() {

    override fun areItemsTheSame(oldItem: MusicEntity, newItem: MusicEntity): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: MusicEntity, newItem: MusicEntity): Boolean {
        return oldItem == newItem
    }
}