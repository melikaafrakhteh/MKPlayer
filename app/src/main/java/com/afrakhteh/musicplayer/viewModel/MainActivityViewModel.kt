package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import com.afrakhteh.musicplayer.model.repository.MusicRepository
import com.afrakhteh.musicplayer.views.state.MusicState
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
        var repository: MusicRepository
) : ViewModel() {


    private val pState = SingleLiveEvent<MusicState>()
    val state: LiveData<MusicState> get() = pState

    fun fetchAllMusic() {
        val resultList: List<MusicEntity> = repository.getAllMusic()
        pState.setValue(MusicState(resultList))
    }

}