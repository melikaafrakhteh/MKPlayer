package com.afrakhteh.musicplayer.di.modules

import android.content.Context
import com.afrakhteh.musicplayer.di.scopes.PlayerScope
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import dagger.Module
import dagger.Provides


@Module
class ExoPlayerModule {

    @PlayerScope
    @Provides
    fun providerAudioAttributes() = AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

    @PlayerScope
    @Provides
    fun providerMediaSourceFactory(context: Context): MediaSourceFactory {
        return DefaultMediaSourceFactory(context)
    }

    @PlayerScope
    @Provides
    fun provideTrackSelector(context: Context): TrackSelector {
        return DefaultTrackSelector(context)
    }

    @PlayerScope
    @Provides
    fun providerExoPlayer(context: Context): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context).build().apply {
            //    setMediaSourceFactory(mediaSourceFactory)
            setAudioAttributes(audioAttributes, true)
            //     setTrackSelector(trackSelector)
        }
    }
}