package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.util.SingleEvent
import com.afrakhteh.musicplayer.views.mainActivity.state.MusicState
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private var repository: MusicRepository
) : ViewModel() {
    private val pState = MutableLiveData(MusicState())
    val state: LiveData<MusicState> get() = pState

    fun fetchAllMusic() {
        try {
            val resultList: List<MusicEntity> = repository.getAllMusic()
            pState.value = state.value?.copy(musicItems = resultList)
        } catch (e: Exception) {
            pState.value =
                state.value?.copy(message = SingleEvent("There was an error while receiving the musics"))
        }
    }
}