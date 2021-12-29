package com.afrakhteh.musicplayer.views.main.adapters.allMusic.dialog

import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.databinding.PlaylistItemRowBinding
import com.afrakhteh.musicplayer.model.entity.audio.AllPlayListEntity

class PlayListDialogViewHolder(
        private val binding: PlaylistItemRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: AllPlayListEntity,
             musicPosition: Int,
             onClick: (Int, Int) -> Unit) {
        binding.apply {
            playlistDialogItemRowTextView.text = data.title
            playlistDialogItemRowLinear.setOnClickListener {
                onClick.invoke(data.id!!, musicPosition)
            }
        }
    }
}