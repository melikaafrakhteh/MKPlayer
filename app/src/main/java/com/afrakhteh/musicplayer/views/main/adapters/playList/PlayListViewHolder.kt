package com.afrakhteh.musicplayer.views.main.adapters.playList

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.db.PlayListEntity
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlayListViewHolder(
        val binding: MusicItemRowBinding,
        val context: Context,
        val repository: PlayListRepository
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
            data: PlayListEntity,
            onclick: (Int) -> Unit,
            onDelete: (Int) -> Unit) {
        binding.apply {
            musicItemRowImageIv.setImageResource(R.drawable.dog)
            musicItemRowNameTv.text = data.title
            //for play list it shows number of musics in play list instead of singer
            GlobalScope.launch(Dispatchers.Main) {
                repository.getPlayListWithMusics(absoluteAdapterPosition).size.let {
                    musicItemRowSingerTv.text = it.toString()
                }
            }
            musicItemRowLinear.setOnClickListener { onclick.invoke(absoluteAdapterPosition) }
            musicItemRowImageMenuIv.setOnClickListener {
                PopupMenu(context, it).run {
                    menuInflater.inflate(R.menu.popup_menu, menu)
                    setOnMenuItemClickListener {
                        onDelete.invoke(absoluteAdapterPosition)
                        true
                    }
                    show()
                }
            }
        }
    }
}