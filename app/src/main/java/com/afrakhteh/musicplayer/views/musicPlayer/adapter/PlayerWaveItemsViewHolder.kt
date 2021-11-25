package com.afrakhteh.musicplayer.views.musicPlayer.adapter


import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.databinding.WaveItemRowBinding
import com.afrakhteh.musicplayer.model.entity.WaveModel

class PlayerWaveItemsViewHolder(
        private val binding: WaveItemRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: WaveModel) {
        with(binding) {
            waveItemRow.activePercents = data.percent
            waveItemRow.isActive = data.isActive
        }
    }
}