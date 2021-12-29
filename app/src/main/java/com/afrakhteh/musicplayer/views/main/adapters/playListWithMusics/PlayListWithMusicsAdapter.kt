package com.afrakhteh.musicplayer.views.main.adapters.playListWithMusics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository

class PlayListWithMusicsAdapter(
        private val click: (Int) -> Unit,
        private val onRemoveMusic: (Int) -> Unit,
        val repository: MusicRepository

) : ListAdapter<MusicEntity, PlayListWithMusicsViewHolder>(PlayListWithMusicsDiffCallBack()) {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): PlayListWithMusicsViewHolder {
        return PlayListWithMusicsViewHolder(
                MusicItemRowBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                ),
                parent.context,
                repository
        )
    }

    override fun onBindViewHolder(holder: PlayListWithMusicsViewHolder, position: Int) {
        holder.bind(getItem(position), click, onRemoveMusic)
    }

    override fun onViewAttachedToWindow(holder: PlayListWithMusicsViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.loadAlbumArt(getItem(holder.absoluteAdapterPosition).path)
    }

    override fun onViewDetachedFromWindow(holder: PlayListWithMusicsViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.stopLoadingAlbumArt()
    }
}