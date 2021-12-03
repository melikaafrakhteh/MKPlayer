package com.afrakhteh.musicplayer.views.main.adapters.recently


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository

class RecentlyAdapter(
        private val click: (Int) -> Unit,
        private val repository: MusicRepository
) : ListAdapter<MusicEntity, RecentlyViewHolder>(RecentlyDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentlyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MusicItemRowBinding.inflate(inflater, parent, false)
        return RecentlyViewHolder(binding, repository)
    }

    override fun onBindViewHolder(holder: RecentlyViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }

    override fun onViewAttachedToWindow(holder: RecentlyViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.loadArtAlbum(getItem(holder.absoluteAdapterPosition).path)
    }

    override fun onViewDetachedFromWindow(holder: RecentlyViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.stopLoadingArtAlbum()
    }
}