package com.afrakhteh.musicplayer.views.mainActivity.adapters.allMusic

import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AllMusicViewHolder(
        private val binding: MusicItemRowBinding,
        private val repository: MusicRepository) :
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
        loadingArt = GlobalScope.launch(Dispatchers.Main) {
            repository.getMusicArtPicture(path)?.let { artMusicBytes ->
                binding.musicItemRowImageIv
                        .setImageBitmap(BitmapFactory.decodeByteArray(
                                artMusicBytes,
                                0,
                                artMusicBytes.size)
                        )
            }
        }
    }

    fun cancelLoadingArtPicture() {
        loadingArt?.cancel()
    }
}