package com.afrakhteh.musicplayer.views.main.adapters.playList

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.AllPlayListEntity
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository

class PlayListViewHolder(
        val binding: MusicItemRowBinding,
        val context: Context,
        val repository: PlayListRepository
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(
            data: AllPlayListEntity,
            onclick: (Int) -> Unit,
            onDelete: (Int) -> Unit) {
        binding.apply {
            musicItemRowImageIv.setImageResource(R.drawable.emptypic)
            musicItemRowNameTv.text = data.title
            musicItemRowLinear.setOnClickListener { onclick.invoke(absoluteAdapterPosition) }
            musicItemRowImageMenuIv.setOnClickListener { makePopUpMenu(it, absoluteAdapterPosition, onDelete) }

            //for play list it shows number of musics in play list instead of singer
                val number = data.size
                musicItemRowSingerTv.text = "$number song" + if (number == 1) "" else "s"

        }
    }

    private fun makePopUpMenu(it: View , id: Int, onDelete: (Int) -> Unit) {
        PopupMenu(context, it).run {
            menuInflater.inflate(R.menu.popup_menu, menu)
            setOnMenuItemClickListener {
                onDelete.invoke(id)
                true
            }
            show()
        }
    }

}
