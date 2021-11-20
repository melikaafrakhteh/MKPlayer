package com.afrakhteh.musicplayer.di.modules

import com.afrakhteh.musicplayer.views.presenter.ArtAlbumPresenter
import com.afrakhteh.musicplayer.views.presenter.ArtAlbumPresenterImpl
import dagger.Binds
import dagger.Module


@Module
interface PresenterModule {

    @Binds
    fun bindArtAlbumPresenter(artAlbumPresenterImpl: ArtAlbumPresenterImpl): ArtAlbumPresenter

}