package com.afrakhteh.musicplayer.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.di.scopes.PlayerActivityScope
import com.afrakhteh.musicplayer.model.sharedPrefrences.PreferenceManager
import com.afrakhteh.musicplayer.model.sharedPrefrences.PreferenceManagerImpl
import dagger.Module
import dagger.Provides


@Module
class SharedPrefModule {

    @PlayerActivityScope
    @Provides
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            Strings.SHARED_PREF_NAME, Context.MODE_PRIVATE
        )
    }

    @PlayerActivityScope
    @Provides
    fun provideSharedPreferenceEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @PlayerActivityScope
    @Provides
    fun provideSharedPreferenceManager(
        sharedPreferences: SharedPreferences,
        editor: SharedPreferences.Editor
    ): PreferenceManager {
        return PreferenceManagerImpl(sharedPreferences, editor)
    }

}