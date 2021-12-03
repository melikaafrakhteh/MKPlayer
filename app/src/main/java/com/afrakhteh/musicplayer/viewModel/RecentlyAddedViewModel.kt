package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.util.SingleEvent
import com.afrakhteh.musicplayer.views.main.state.MusicState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecentlyAddedViewModel @Inject constructor(
        val repository: MusicRepository
) : ViewModel() {

    private val pRecentlyAddedState = MutableLiveData(MusicState())
    val recentlyAddedState: LiveData<MusicState> get() = pRecentlyAddedState

    private var recentlyAddedJob: Job? = null

    fun fetchRecentlyAddedMusic() {
        try {
            recentlyAddedJob = CoroutineScope(Dispatchers.IO).launch {
                val result = repository.getRecentlyMusic()
                pRecentlyAddedState.postValue(recentlyAddedState.value?.copy(musicItems = result))
            }
        } catch (e: Exception) {
            pRecentlyAddedState.value = recentlyAddedState.value?.copy(
                    message = SingleEvent(
                            "There was an error while receiving the Recently Added musics"
                    )
            )
        }
    }

    override fun onCleared() {
        recentlyAddedJob?.cancel()
        super.onCleared()
    }
}