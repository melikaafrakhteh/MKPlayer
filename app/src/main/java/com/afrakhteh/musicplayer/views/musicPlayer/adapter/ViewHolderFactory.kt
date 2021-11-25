package com.afrakhteh.musicplayer.views.musicPlayer.adapter

import android.view.ViewGroup
import com.afrakhteh.musicplayer.constant.Numerals

class ViewHolderFactory(
        private val parent: ViewGroup,
        private val viewType: Int,
        private val screenHeight: Int
) {

    fun create(): PlayerViewHolder {
        return when (viewType) {
            Numerals.EMPTY_VIEW_TYPE -> {
                PlayerEmptyWaveItemViewHolder.create(screenHeight, parent)
            }
            else -> {
                PlayerWaveItemsViewHolder.create(parent)
            }
        }
    }
}