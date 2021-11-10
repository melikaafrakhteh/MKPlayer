package com.afrakhteh.musicplayer.di.modules

import android.content.Context
import com.afrakhteh.musicplayer.di.scopes.PlayerScope
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
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
    fun providerExoPlayer(
            context: Context,
            audioAttributes: AudioAttributes
    ): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context).build().apply {
            setAudioAttributes(audioAttributes, true)
        }
    }
}