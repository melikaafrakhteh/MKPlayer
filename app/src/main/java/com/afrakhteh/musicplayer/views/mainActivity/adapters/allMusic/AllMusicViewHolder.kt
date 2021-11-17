package com.afrakhteh.musicplayer.views.mainActivity.adapters.allMusic

import android.content.Context
import android.graphics.Bitmap
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
            musicItemRowImageIv.setImageDrawable(null)
            musicItemRowLinear.setOnClickListener { click.invoke(absoluteAdapterPosition) }
            musicItemRowNameTv.text = data.name
            musicItemRowSingerTv.text = data.artist
        }
    }

    fun loadArtPicture(path: String) {
        loadingArt = CoroutineScope(Dispatchers.IO).launch {
            repository.getMusicArtPicture(path).let { artMusicBytes ->
                if (artMusicBytes == null) {
                    withContext(Dispatchers.Main) {
                        Glide.with(context).load(R.drawable.minimusic).into(binding.musicItemRowImageIv)
                        return@withContext
                    }
                    return@let
                }
                try {
                    val bitMap = resizeBitMap(BitmapFactory.decodeByteArray(
                            artMusicBytes,
                            0,
                            artMusicBytes.size))
                    withContext(Dispatchers.Main) {
                        binding.musicItemRowImageIv
                                .setImageBitmap(bitMap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun resizeBitMap(image: Bitmap, maxSize: Int = 512): Bitmap {
        var width = image.width
        var height = image.height
        val ratio = width.toFloat() / height.toFloat()
        if (ratio > 1) {
            width = maxSize
            height = (width / ratio).toInt()
        } else {
            height = maxSize
            width = (height * ratio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun cancelLoadingArtPicture() {
        loadingArt?.cancel()
    }
}