package com.afrakhteh.musicplayer.views.main.adapters.allMusic

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

class AllMusicViewHolder(
        private val binding: MusicItemRowBinding,
        private val repository: MusicRepository,
        private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

    var loadingArt: Job? = null

    fun bind(data: MusicEntity,
             click: (Int) -> Unit,
             addToPlayList: (Int) -> Unit,
             delete: (Int) -> Unit) {
        with(binding) {
            musicItemRowImageIv.setImageDrawable(null)
            musicItemRowLinear.setOnClickListener { click.invoke(absoluteAdapterPosition) }
            musicItemRowNameTv.text = data.name
            musicItemRowSingerTv.text = data.artist
            musicItemRowImageMenuIv.setOnClickListener {
                val listPopupWindow = setUpPopUpMenu()
                listPopupWindow.apply {
                    setOnItemClickListener { _, _, position, _ ->
                        when (position) {
                            0 -> addToPlayList.invoke(absoluteAdapterPosition)
                            1 -> delete.invoke(absoluteAdapterPosition)
                        }
                        this.dismiss()
                    }
                    show()
                }
            }
        }
    }

    private fun setUpPopUpMenu(): ListPopupWindow {
        val listPopupWindow = ListPopupWindow(context)
        val menuListAdapter = ArrayAdapter(
                context,
                R.layout.popup_menu_item,
                Lists.POPUP_MENU_ITEMS_LIST
        )
        listPopupWindow.apply {
            anchorView = binding.musicItemRowImageMenuIv
            isModal = true
            setAdapter(menuListAdapter)
            width = menuListAdapter.measureContentWidth(context)
        }
        return listPopupWindow
    }

    fun loadArtPicture(path: String) {
        loadingArt = CoroutineScope(Dispatchers.IO).launch {
            repository.getMusicArtPicture(path).let { artMusicBytes ->
                if (artMusicBytes == null) {
                    withContext(Dispatchers.Main) {
                        binding.musicItemRowImageIv.setImageResource(R.drawable.dog)
                        return@withContext
                    }
                    return@let
                }
                try {
                    val bitMap = artMusicBytes.toBitmap().resize()
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

    fun cancelLoadingArtPicture() {
        loadingArt?.cancel()
    }
}