package com.afrakhteh.musicplayer.constant

import com.afrakhteh.musicplayer.R

object Lists {
    val SUPPORTED_EXT = listOf(
            "mp3",
            "wav",
            "3gpp",
            "3gp",
            "amr",
            "aac",
            "m4a",
            "mid",
            "ogg"
    )

    val VOLUME_IMAGE_LIST = arrayListOf(
            R.drawable.volume_mute,
            R.drawable.ic_volume_down,
            R.drawable.ic_volume_mid,
            R.drawable.ic_volume_up
    )

    val VOLUME_PB_LIST = arrayListOf(
            0,
            10,
            50,
            100
    )

    val VOLUME_LIST = arrayListOf(
            0.0f,
            0.1f,
            0.5f,
            1.0f
    )

    val POPUP_MENU_ITEMS_LIST = arrayListOf(
            "Add To PlayList",
            "Delete"
    )

    val POPUP_MENU_ITEMS_CHOOSE_PLAY_LIST = arrayListOf(
            "Add To Existing PlayList",
            "Create A New"
    )

    val POPUP_MENU_ITEMS_CHOOSE_PLAY_LIST_IF_EMPTY = arrayListOf(
            "Create A New"
    )
}