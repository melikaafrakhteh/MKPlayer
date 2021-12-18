package com.afrakhteh.musicplayer.views.main.adapters.allMusic.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.databinding.PlaylistItemRowBinding
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity


class PlayListDialogAdapter(
        private val click: (Int, Int) -> Unit,
        private val musicPosition: Int
) : ListAdapter<PlayListEntity, PlayListDialogViewHolder>(PlayListDialogDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListDialogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistItemRowBinding.inflate(inflater, parent, false)
        return PlayListDialogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListDialogViewHolder, position: Int) {
        holder.bind(getItem(position), musicPosition, click)
    }
}