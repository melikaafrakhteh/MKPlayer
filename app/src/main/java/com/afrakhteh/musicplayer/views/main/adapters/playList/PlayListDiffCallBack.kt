package com.afrakhteh.musicplayer.views.main.adapters.playList

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity

class PlayListDiffCallBack : DiffUtil.ItemCallback<PlayListEntity>() {
    override fun areItemsTheSame(oldItem: PlayListEntity, newItem: PlayListEntity): Boolean {
        return oldItem.playListId == newItem.playListId
    }

    override fun areContentsTheSame(oldItem: PlayListEntity, newItem: PlayListEntity): Boolean {
        return oldItem == newItem
    }
}