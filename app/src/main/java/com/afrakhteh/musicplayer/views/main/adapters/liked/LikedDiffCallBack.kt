package com.afrakhteh.musicplayer.views.main.adapters.liked

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity

class LikedDiffCallBack : DiffUtil.ItemCallback<MusicEntity>() {
    override fun areItemsTheSame(oldItem: MusicEntity, newItem: MusicEntity): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: MusicEntity, newItem: MusicEntity): Boolean {
        return newItem == oldItem
    }
}