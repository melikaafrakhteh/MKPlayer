package com.afrakhteh.musicplayer.di.modules

import android.content.Context
import com.afrakhteh.musicplayer.model.sharedPrefrences.PreferenceManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefModule {

    @Singleton
    @Provides
    fun provideSharedPreferenceManager(context: Context): PreferenceManagerImpl {
        return PreferenceManagerImpl(context)
    }
}