package com.afrakhteh.musicplayer.di.components

import com.afrakhteh.musicplayer.di.modules.DataBaseModule
import com.afrakhteh.musicplayer.di.scopes.DBScope
import com.afrakhteh.musicplayer.model.db.FavoriteDao
import com.afrakhteh.musicplayer.model.db.PlayListDao
import dagger.Component

@DBScope
@Component(
        modules = [DataBaseModule::class],
        dependencies = [ApplicationComponent::class])
interface DataBaseComponent {
    fun exposeDao(): FavoriteDao
    fun exposePLayListDao(): PlayListDao
}