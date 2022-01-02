package com.afrakhteh.musicplayer.views.main.adapters.liked

import android.content.Context
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.Lists
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.util.resize
import com.afrakhteh.musicplayer.util.toBitmap
import com.afrakhteh.musicplayer.views.util.measureContentWidth
import kotlinx.coroutines.*

class LikedViewHolder(
        private val binding: MusicItemRowBinding,
        private val repository: MusicRepository,
        private val context: Context
) : RecyclerView.ViewHolder(binding.root) {
    private var loadArtByteJob: Job? = null

    fun bind(data: MusicEntity,
             click: (Int) -> Unit,
             removeFromLike: (String) -> Unit,
             delete: (Int) -> Unit
    ) {
        with(binding) {
            musicItemRowImageIv.setImageDrawable(null)
            musicItemRowNameTv.text = data.name
            musicItemRowSingerTv.text = data.artist
            musicItemRowLinear.setOnClickListener { click.invoke(absoluteAdapterPosition) }
            musicItemRowImageMenuIv.setOnClickListener { chooseMenuItem(data, removeFromLike, delete) }
        }
    }

    private fun chooseMenuItem(
            data: MusicEntity,
            removeFromLike: (String) -> Unit,
            delete: (Int) -> Unit
    ) {
        val listPopupWindow = ListPopupWindow(context)
        val arrayAdapter = ArrayAdapter(
                context,
                R.layout.popup_menu_item,
                Lists.POPUP_MENU_ITEMS_LIKED_FRAGMENT)
        listPopupWindow.apply {
            anchorView = binding.musicItemRowImageMenuIv
            setAdapter(arrayAdapter)
            isModal = true
            width = arrayAdapter.measureContentWidth(context)
            setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> {
                        removeFromLike.invoke(data.path)
                        dismiss()
                    }
                    1 -> {
                        delete.invoke(absoluteAdapterPosition)
                        dismiss()
                    }
                }
            }
            show()
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