package com.afrakhteh.musicplayer.views.main.adapters.playListWithMusics

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity

class PlayListWithMusicsDiffCallBack : DiffUtil.ItemCallback<AllMusicsEntity>() {
    override fun areItemsTheSame(
            oldItem: AllMusicsEntity,
            newItem: AllMusicsEntity
    ): Boolean {
        return oldItem.musicId == newItem.musicId
    }

    override fun areContentsTheSame(
            oldItem: AllMusicsEntity,
            newItem: AllMusicsEntity
    ): Boolean {
        return oldItem == newItem
    }
}