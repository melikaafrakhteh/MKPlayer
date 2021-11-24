package com.afrakhteh.musicplayer.views.playMusicActivity.wavesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.databinding.WaveItemRowBinding
import com.afrakhteh.musicplayer.model.entity.WaveModel

class PlayerWaveItemsAdapter
    : ListAdapter<WaveModel, PlayerWaveItemsViewHolder>(PlayerWaveItemsDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerWaveItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WaveItemRowBinding.inflate(inflater, parent, false)
        return PlayerWaveItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerWaveItemsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}