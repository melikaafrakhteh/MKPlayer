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
    private lateinit var job: Job
    private lateinit var serviceViewInterface: AudioServiceViewInterface

    override fun setService(service: AudioServiceViewInterface) {
        serviceViewInterface = service
    }

    override fun getAudioByteArray() {
        job = CoroutineScope(Dispatchers.IO).launch {
            if (serviceViewInterface.getPlayingPosition() == null) return@launch
            serviceViewInterface.getMusicList()[serviceViewInterface.getPlayingPosition()!!].let {
                val byteArray = repository.getMusicArtPicture(it.path)!!
                serviceViewInterface.setAudioByteArray(byteArray)
            }
        }
    }

    override fun onDestroy() {
        serviceViewInterface = null!!
        job.cancel()
    }

}