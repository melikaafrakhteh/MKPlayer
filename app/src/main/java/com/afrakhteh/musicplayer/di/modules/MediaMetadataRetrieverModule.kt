package com.afrakhteh.musicplayer.di.modules

import android.media.MediaMetadataRetriever
import dagger.Module
import dagger.Provides

@Module
class MediaMetadataRetrieverModule {

    @Provides
    fun provideMediaMetaData(): MediaMetadataRetriever {
        return MediaMetadataRetriever()
    }

}