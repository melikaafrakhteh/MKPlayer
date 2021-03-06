package com.afrakhteh.musicplayer.views.main.adapters.liked

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository

class LikedAdapter(
        private val click: (Int) -> Unit,
        private val removeFromLike: (String) -> Unit,
        private val delete: (Int) -> Unit,
        private val repository: MusicRepository
) : ListAdapter<MusicEntity, LikedViewHolder>(LikedDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MusicItemRowBinding.inflate(inflater, parent, false)
        return LikedViewHolder(binding, repository, parent.context)
    }

    override fun onBindViewHolder(holder: LikedViewHolder, position: Int) {
        holder.bind(getItem(position), click, removeFromLike, delete)
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