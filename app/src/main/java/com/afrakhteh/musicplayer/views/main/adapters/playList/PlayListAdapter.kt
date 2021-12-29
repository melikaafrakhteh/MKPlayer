package com.afrakhteh.musicplayer.views.main.adapters.playList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.AllPlayListEntity
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository

class PlayListAdapter(
        private val onClick: (Int) -> Unit,
        private val onDeletePlayListClick: (Int) -> Unit,
        val repository: PlayListRepository
) : ListAdapter<AllPlayListEntity, PlayListViewHolder>(PlayListDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MusicItemRowBinding.inflate(inflater, parent, false)
        return PlayListViewHolder(binding, parent.context, repository)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(getItem(position), onClick, onDeletePlayListClick)
    }

    override fun onViewDetachedFromWindow(holder: PlayListViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.diAttachNumberOfSongsJob()
    }
}