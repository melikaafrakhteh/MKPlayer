package com.afrakhteh.musicplayer.di.modules

import android.content.Context
import com.afrakhteh.musicplayer.di.scopes.SharedScope
import com.afrakhteh.musicplayer.model.sharedPrefrences.PreferenceManagerImpl
import dagger.Module
import dagger.Provides

@Module
class SharedPrefModule {

    @SharedScope
    @Provides
    fun provideSharedPreferenceManager(context: Context): PreferenceManagerImpl {
        return PreferenceManagerImpl(context)
    }
}