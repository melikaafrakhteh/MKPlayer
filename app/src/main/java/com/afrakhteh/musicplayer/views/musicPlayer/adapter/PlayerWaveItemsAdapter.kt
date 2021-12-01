package com.afrakhteh.musicplayer.views.musicPlayer.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.model.entity.wave.EmptyWaveModel
import com.afrakhteh.musicplayer.model.entity.wave.WaveItemModel

class PlayerWaveItemsAdapter(
    private val screenHeight: Int,
    private val waveItemList: MutableList<WaveItemModel>
) : RecyclerView.Adapter<PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return ViewHolderFactory(
            parent,
            viewType,
            screenHeight
        ).create()
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(waveItemList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (waveItemList[position] is EmptyWaveModel) Numerals.EMPTY_VIEW_TYPE
        else Numerals.WAVE_ITEM_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return waveItemList.size
    }

    fun submitList(list: MutableList<WaveItemModel>) {
        list.add(0, EmptyWaveModel())
        list.add(EmptyWaveModel())
        waveItemList.clear()
        waveItemList.addAll(list)
        notifyDataSetChanged()
    }


}