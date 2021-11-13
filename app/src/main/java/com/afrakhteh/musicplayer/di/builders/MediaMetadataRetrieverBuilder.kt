package com.afrakhteh.musicplayer.di.builders

import com.afrakhteh.musicplayer.di.components.DaggerMediaMetadataRetrieverComponent
import com.afrakhteh.musicplayer.di.components.MediaMetadataRetrieverComponent
import com.afrakhteh.musicplayer.di.modules.MediaMetadataRetrieverModule

object MediaMetadataRetrieverBuilder
    : ComponentBuilder<MediaMetadataRetrieverComponent>() {
    override fun provideInstance(): MediaMetadataRetrieverComponent {
        return DaggerMediaMetadataRetrieverComponent.builder()
                .mediaMetadataRetrieverModule(MediaMetadataRetrieverModule())
                .build()
    }
}