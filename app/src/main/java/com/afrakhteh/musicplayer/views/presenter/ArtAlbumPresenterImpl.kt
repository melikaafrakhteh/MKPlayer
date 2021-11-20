package com.afrakhteh.musicplayer.views.presenter

import com.afrakhteh.musicplayer.di.scopes.PlayerScope
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.views.services.AudioServiceViewInterface
import kotlinx.coroutines.*
import javax.inject.Inject

@PlayerScope
class ArtAlbumPresenterImpl @Inject constructor(
    private val repository: MusicRepository
) : ArtAlbumPresenter {
    private var job: Job? = null
    private var serviceViewInterface: AudioServiceViewInterface? = null

    override fun setService(service: AudioServiceViewInterface) {
        serviceViewInterface = service
    }

    override fun getAudioByteArray() {
        job = CoroutineScope(Dispatchers.IO).launch {
            if (serviceViewInterface == null) return@launch
            if (requireNotNull(serviceViewInterface).getPlayingPosition() == null) return@launch
            requireNotNull(serviceViewInterface)
                .getMusicList()[requireNotNull(serviceViewInterface)
                .getPlayingPosition()!!].let {
                requireNotNull(serviceViewInterface)
                    .setAudioByteArray(repository.getMusicArtPicture(it.path))
            }
        }
    }

    override fun onDestroy() {
        serviceViewInterface = null
        job?.cancel()
    }

}