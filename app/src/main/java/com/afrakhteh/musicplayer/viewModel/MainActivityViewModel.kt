package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import com.afrakhteh.musicplayer.model.repository.MusicRepository
import com.afrakhteh.musicplayer.model.repository.MusicRepositoryImpl
import com.afrakhteh.musicplayer.util.MusicState

class MainActivityViewModel(
    var repository: MusicRepository
) : ViewModel() {

    private val state = MutableLiveData<MusicState>()
    val musicState: LiveData<MusicState> get() = state

    fun fetchAllMusic() {
        val resultList: List<MusicEntity> = repository.getAllMusic()
        state.value = MusicState(resultList, 0)

    }

}