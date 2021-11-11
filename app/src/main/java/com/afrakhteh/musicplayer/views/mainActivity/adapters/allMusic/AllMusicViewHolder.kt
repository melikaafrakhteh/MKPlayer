package com.afrakhteh.musicplayer.views.mainActivity.adapters.allMusic

import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.MusicEntity

class AllMusicViewHolder(private val binding: MusicItemRowBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: MusicEntity, click: (Int) -> Unit) {
        with(binding) {
            musicItemRowLinear.setOnClickListener { click.invoke(absoluteAdapterPosition) }
            musicItemRowNameTv.text = data.name
            musicItemRowSingerTv.text = data.artist
        }
    }
}