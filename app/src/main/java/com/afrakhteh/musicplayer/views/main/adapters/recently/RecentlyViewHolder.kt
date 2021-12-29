package com.afrakhteh.musicplayer.views.main.adapters.recently

import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.util.resize
import com.afrakhteh.musicplayer.util.toBitmap
import kotlinx.coroutines.*

class RecentlyViewHolder(
        private val binding: MusicItemRowBinding,
        private val repository: MusicRepository
) : RecyclerView.ViewHolder(binding.root) {

    private var artAlbumImage: Job? = null

    fun bind(data: MusicEntity, click: (Int) -> Unit) {
        with(binding) {
            musicItemRowImageIv.setImageDrawable(null)
            musicItemRowSingerTv.text = data.artist
            musicItemRowNameTv.text = data.name
            musicItemRowLinear.setOnClickListener { click.invoke(absoluteAdapterPosition) }
        }
    }

    fun loadArtAlbum(path: String) {
        artAlbumImage = CoroutineScope(Dispatchers.IO).launch {
            repository.getMusicArtPicture(path).let { artAlbumByteArray ->
                if (artAlbumByteArray == null) {
                    withContext(Dispatchers.Main) {
                        binding.musicItemRowImageIv.setImageResource(R.drawable.emptypic)
                        return@withContext
                    }
                    return@let
                }
                try {
                    val bitmap = artAlbumByteArray.toBitmap().resize()
                    withContext(Dispatchers.Main) {
                        binding.musicItemRowImageIv.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun stopLoadingArtAlbum() {
        artAlbumImage?.cancel()
    }

}