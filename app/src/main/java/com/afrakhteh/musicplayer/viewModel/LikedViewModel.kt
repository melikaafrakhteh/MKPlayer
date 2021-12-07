package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.useCase.GetAllFaveListUseCase
import javax.inject.Inject


class LikedViewModel @Inject constructor(
        getAllFaveListUseCase: GetAllFaveListUseCase,
        val repository: MusicRepository
) : ViewModel() {

    val listOfLikedAudio: LiveData<List<AudioPrePareToPlay>> =
            getAllFaveListUseCase.invoke().asLiveData()
}
