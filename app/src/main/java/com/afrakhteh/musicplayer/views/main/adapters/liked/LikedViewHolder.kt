package com.afrakhteh.musicplayer.views.main.adapters.liked

import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.util.resize
import com.afrakhteh.musicplayer.util.toBitmap
import kotlinx.coroutines.*

class LikedViewHolder(
        private val binding: MusicItemRowBinding,
        private val repository: MusicRepository
) : RecyclerView.ViewHolder(binding.root) {
    private var loadArtByteJob: Job? = null

    fun bind(data: MusicEntity, click: (Int) -> Unit) {
        with(binding) {
            musicItemRowImageIv.setImageDrawable(null)
            musicItemRowNameTv.text = data.name
            musicItemRowSingerTv.text = data.artist
            musicItemRowLinear.setOnClickListener { click.invoke(absoluteAdapterPosition) }
        }
    }

    fun loadImages(path: String) {
        loadArtByteJob = CoroutineScope(Dispatchers.IO).launch {
            repository.getMusicArtPicture(path).let {
                if (it == null) {
                    withContext(Dispatchers.Main) {
                       binding.musicItemRowImageIv.setImageResource(R.drawable.emptypic)
                        return@withContext
                    }
                    return@let
                }
                try {
                    val bitmap = it.toBitmap().resize()
                    withContext(Dispatchers.Main) {
                       binding.musicItemRowImageIv.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun cancelLoadingImages() {
        loadArtByteJob?.cancel()
    }
}