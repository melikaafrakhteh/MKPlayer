package com.afrakhteh.musicplayer.views.main.adapters.allMusic


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository

class AllMusicAdapter(
        private val click: (Int) -> Unit,
        private val musicRepository: MusicRepository
) : ListAdapter<MusicEntity, AllMusicViewHolder>(AllMusicDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMusicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MusicItemRowBinding.inflate(inflater, parent, false)
        return AllMusicViewHolder(binding, musicRepository, parent.context)
    }

    override fun onBindViewHolder(holder: AllMusicViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }

    override fun onViewAttachedToWindow(holder: AllMusicViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.loadArtPicture(getItem(holder.absoluteAdapterPosition).path)
    }

    override fun onViewDetachedFromWindow(holder: AllMusicViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.cancelLoadingArtPicture()
    }
}