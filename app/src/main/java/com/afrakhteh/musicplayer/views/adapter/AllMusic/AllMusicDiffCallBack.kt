package com.afrakhteh.musicplayer.views.adapter.AllMusic

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.MusicEntity

class AllMusicDiffCallBack : DiffUtil.ItemCallback<MusicEntity>() {

    override fun areItemsTheSame(oldItem: MusicEntity, newItem: MusicEntity): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: MusicEntity, newItem: MusicEntity): Boolean {
        return oldItem == newItem
    }
}