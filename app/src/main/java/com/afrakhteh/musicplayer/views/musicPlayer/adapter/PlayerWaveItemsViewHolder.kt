package com.afrakhteh.musicplayer.views.musicPlayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.afrakhteh.musicplayer.databinding.WaveItemRowBinding
import com.afrakhteh.musicplayer.model.entity.wave.WaveItemModel
import com.afrakhteh.musicplayer.model.entity.wave.WaveModel

class PlayerWaveItemsViewHolder(
        private val binding: WaveItemRowBinding
) : PlayerViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): PlayerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = WaveItemRowBinding.inflate(inflater, parent, false)
            return PlayerWaveItemsViewHolder(binding)
        }
    }

    override fun bind(data: WaveItemModel) {
        if (data !is WaveModel) return
        with(binding) {
            waveItemRow.activePercents = data.percent
            waveItemRow.isActive = data.isActive
        }
    }
}