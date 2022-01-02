package com.afrakhteh.musicplayer.views.main.adapters.recently

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
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
        private val repository: MusicRepository,
        private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    private var artAlbumImage: Job? = null

    fun bind(data: MusicEntity,
             click: (Int) -> Unit,
             onDeleteClick: (Int) -> Unit
    ) {
        with(binding) {
            musicItemRowImageIv.setImageDrawable(null)
            musicItemRowSingerTv.text = data.artist
            musicItemRowNameTv.text = data.name
            musicItemRowLinear.setOnClickListener { click.invoke(absoluteAdapterPosition) }
            musicItemRowImageMenuIv.setOnClickListener { deleteMusic(it, onDeleteClick) }
        }
    }

    private fun deleteMusic(view: View, onDeleteClick: (Int) -> Unit) {
        PopupMenu(context, view).run {
            menuInflater.inflate(R.menu.remove_music_popup_menu, menu)
            setOnMenuItemClickListener {
                onDeleteClick.invoke(absoluteAdapterPosition)
                true
            }
            show()
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