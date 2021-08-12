package com.afrakhteh.musicplayer.model.repository

import android.net.Uri
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import java.net.URL

interface MusicRepository {

    fun getAllMusic(): List<MusicEntity>
}