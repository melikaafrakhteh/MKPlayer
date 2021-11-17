package com.afrakhteh.musicplayer.views.mainActivity.adapters.allMusic

import android.content.Context
import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.bumptech.glide.Glide
import kotlinx.coroutines.*

class AllMusicViewHolder(
        private val binding: MusicItemRowBinding,
        private val repository: MusicRepository,
        private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

    var loadingArt: Job? = null

    fun bind(data: MusicEntity, click: (Int) -> Unit) {
        with(binding) {
            musicItemRowLinear.setOnClickListener { click.invoke(absoluteAdapterPosition) }
            musicItemRowNameTv.text = data.name
            musicItemRowSingerTv.text = data.artist
        }
    }

    fun loadArtPicture(path: String) {
        loadingArt = CoroutineScope(Dispatchers.IO).launch {
            repository.getMusicArtPicture(path).let { artMusicBytes ->
                withContext(Dispatchers.Main) {
                    if (artMusicBytes == null) {
                        Glide.with(context).load(R.drawable.minimusic).into(binding.musicItemRowImageIv)
                    } else {
                        binding.musicItemRowImageIv
                                .setImageBitmap(BitmapFactory.decodeByteArray(
                                        artMusicBytes,
                                        0,
                                        artMusicBytes.size)
                                )
                    }
                }
            }
        }
    }

    fun cancelLoadingArtPicture() {
        loadingArt?.cancel()
    }
}