package com.afrakhteh.musicplayer.views.main.adapters.allMusic.dialog

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity

class PlayListDialogDiffCallBack : DiffUtil.ItemCallback<PlayListEntity>() {
    override fun areItemsTheSame(oldItem: PlayListEntity, newItem: PlayListEntity): Boolean {
        return oldItem.playListId == newItem.playListId
    }

    override fun areContentsTheSame(oldItem: PlayListEntity, newItem: PlayListEntity): Boolean {
        return oldItem == newItem
    }
}