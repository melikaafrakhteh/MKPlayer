package com.afrakhteh.musicplayer.views.main.adapters.playListWithMusics

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.util.resize
import com.afrakhteh.musicplayer.util.toBitmap
import kotlinx.coroutines.*

class PlayListWithMusicsViewHolder(
        private val binding: MusicItemRowBinding,
        private val context: Context,
        private val musicRepository: MusicRepository
) : RecyclerView.ViewHolder(binding.root) {

    private var albumArtJob: Job? = null

    fun bind(
        data: MusicEntity,
        onClick: (Int) -> Unit,
        onRemoveMusic: (Int) -> Unit
    ) {
        binding.apply {
            musicItemRowImageMenuIv.setImageDrawable(null)
            musicItemRowNameTv.text = data.name
            musicItemRowSingerTv.text = data.artist
            musicItemRowLinear.setOnClickListener { onClick.invoke(absoluteAdapterPosition) }
            musicItemRowImageMenuIv.setOnClickListener {
                PopupMenu(context, it).run {
                    menuInflater.inflate(R.menu.remove_popup_menu, menu)
                    setOnMenuItemClickListener {
                        onRemoveMusic.invoke(absoluteAdapterPosition)
                        true
                    }
                    show()
                }
            }
        }
    }

    fun loadAlbumArt(path: String) {
        albumArtJob = CoroutineScope(Dispatchers.IO).launch {
            musicRepository.getMusicArtPicture(path).let {
                if (it == null) {
                    withContext(Dispatchers.Main) {
                        binding.musicItemRowImageIv.setImageResource(R.drawable.emptypic)
                        return@withContext
                    }
                    return@let
                }
                val bitmap = it.toBitmap().resize()
                withContext(Dispatchers.Main) {
                    binding.musicItemRowImageIv.setImageBitmap(bitmap)
                }
            }
        }
    }

    fun stopLoadingAlbumArt() {
        albumArtJob?.cancel()
    }
}