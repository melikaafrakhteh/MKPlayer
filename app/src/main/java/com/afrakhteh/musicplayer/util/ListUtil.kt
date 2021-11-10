package com.afrakhteh.musicplayer.util

import com.afrakhteh.musicplayer.model.entity.AudioPrePareToPlay

object ListUtil {
    var list: List<AudioPrePareToPlay>? = emptyList()

    @JvmName("getList1")
    fun getList(): List<AudioPrePareToPlay>? {
        return list
    }

    @JvmName("setList1")
    fun setList(list: List<AudioPrePareToPlay>?) {
        this.list = list
    }
}