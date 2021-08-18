package com.afrakhteh.musicplayer.views.adapter.AllMusic


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.MusicEntity

class AllMusicAdapter(
        private val click: (MusicEntity) -> Unit
) : ListAdapter<MusicEntity, AllMusicViewHolder>(AllMusicDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMusicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MusicItemRowBinding.inflate(inflater, parent, false)
        return AllMusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllMusicViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }
}