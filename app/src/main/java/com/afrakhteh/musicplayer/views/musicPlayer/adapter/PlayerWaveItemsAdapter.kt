package com.afrakhteh.musicplayer.views.musicPlayer.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.model.entity.wave.EmptyWaveModel
import com.afrakhteh.musicplayer.model.entity.wave.WaveItemModel

class PlayerWaveItemsAdapter(
        private val screenHeight: Int
) : ListAdapter<WaveItemModel, PlayerViewHolder>(PlayerWaveItemsDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return ViewHolderFactory(
                parent,
                viewType,
                screenHeight
        ).create()
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is EmptyWaveModel) Numerals.EMPTY_VIEW_TYPE
        else Numerals.WAVE_ITEM_VIEW_TYPE
    }

    override fun submitList(list: MutableList<WaveItemModel>?) {
        list?.add(0, EmptyWaveModel())
        list?.add(EmptyWaveModel())
        super.submitList(list)
    }
}