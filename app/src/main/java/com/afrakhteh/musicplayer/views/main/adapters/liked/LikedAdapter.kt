package com.afrakhteh.musicplayer.views.main.adapters.liked

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository

class LikedAdapter(
        private val click: (Int) -> Unit,
        private val repository: MusicRepository
) : ListAdapter<AudioPrePareToPlay, LikedViewHolder>(LikedDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MusicItemRowBinding.inflate(inflater, parent, false)
        return LikedViewHolder(binding, repository)
    }

    override fun onBindViewHolder(holder: LikedViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }

    override fun onViewAttachedToWindow(holder: LikedViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.loadImages(getItem(holder.absoluteAdapterPosition).path)
    }

    override fun onViewDetachedFromWindow(holder: LikedViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.cancelLoadingImages()
    }
}