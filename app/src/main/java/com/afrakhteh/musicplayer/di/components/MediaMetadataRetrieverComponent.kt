package com.afrakhteh.musicplayer.di.components

import android.media.MediaMetadataRetriever
import com.afrakhteh.musicplayer.di.modules.MediaMetadataRetrieverModule
import dagger.Component

@Component(modules = [MediaMetadataRetrieverModule::class])
interface MediaMetadataRetrieverComponent {
    fun expose(): MediaMetadataRetriever
}