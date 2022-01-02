package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.useCase.favorite.DeleteFromFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.favorite.GetAllFaveListUseCase
import com.afrakhteh.musicplayer.views.main.state.MusicState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class LikedViewModel @Inject constructor(
        private val getAllFaveListUseCase: GetAllFaveListUseCase,
        private val deleteFromFaveUseCase: DeleteFromFaveUseCase,
        val repository: MusicRepository
) : ViewModel() {

    private val pState = MutableLiveData<MusicState>()
    val state: LiveData<MusicState> get() = pState

    private val pRemoveMusicFromList = MutableLiveData<Boolean>()
    val removeMusicFromList: LiveData<Boolean> get() = pRemoveMusicFromList

    private var job: Job? = null
    private var deleteJob: Job? = null
    private var removeFromListJob: Job? = null

    fun getAllMusicLikedList() {
        job = CoroutineScope(Dispatchers.IO).launch {
            getAllFaveListUseCase.invoke().let {
                pState.postValue(MusicState(it))
            }
        }
    }

    fun removeMusicFromLikedList(path: String) {
        removeFromListJob = CoroutineScope(Dispatchers.Main).launch {
            deleteFromFaveUseCase.invoke(path)
            pRemoveMusicFromList.postValue(true)
        }
    }

    fun deleteMusic(data: MusicEntity) {
        deleteJob = CoroutineScope(Dispatchers.Main).launch {
            repository.deleteItemFromList(data.path)
            val newList = pState.value?.musicItems as MutableList
            newList.remove(data)
            pState.postValue(pState.value?.copy(musicItems = newList))
        }
    }

    override fun onCleared() {
        job?.cancel()
        deleteJob?.cancel()
        removeFromListJob?.cancel()
        super.onCleared()
    }
}
