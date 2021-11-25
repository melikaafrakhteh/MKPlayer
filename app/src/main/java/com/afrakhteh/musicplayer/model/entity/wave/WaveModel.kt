package com.afrakhteh.musicplayer.model.entity.wave

data class WaveModel(
        val percent: Int,
        val isActive: Boolean
) : WaveItemModel()