package com.afrakhteh.musicplayer.views.main.adapters.allMusic.dialog

import androidx.recyclerview.widget.DiffUtil
import com.afrakhteh.musicplayer.model.entity.audio.AllPlayListEntity

class PlayListDialogDiffCallBack : DiffUtil.ItemCallback<AllPlayListEntity>() {
    override fun areItemsTheSame(oldItem: AllPlayListEntity, newItem: AllPlayListEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AllPlayListEntity, newItem: AllPlayListEntity): Boolean {
        return oldItem == newItem
    }
}