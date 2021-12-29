package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.repository.playList.PlayListRepository
import com.afrakhteh.musicplayer.model.useCase.playList.DeleteOnePlayListUseCase
import com.afrakhteh.musicplayer.model.useCase.playList.GetAllPlayListUseCase
import com.afrakhteh.musicplayer.util.SingleEvent
import com.afrakhteh.musicplayer.views.main.state.PlayListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayListViewModel @Inject constructor(
        private val getAllPlayListUseCase: GetAllPlayListUseCase,
        private val deleteOnePlayListUseCase: DeleteOnePlayListUseCase,
        val repository: PlayListRepository
) : ViewModel() {

    private var playListJob: Job? = null
    private var deletePlayListJob: Job? = null

    private val pState = MutableLiveData<PlayListState>()
    val state: LiveData<PlayListState> get() = pState

    private val pDeletePlayList = MutableLiveData<SingleEvent<Boolean>>()
    val deletePlayList: LiveData<SingleEvent<Boolean>> get() = pDeletePlayList

    fun fetchAllPlayList() {
        playListJob = CoroutineScope(Dispatchers.IO).launch {
            getAllPlayListUseCase.invoke().let {
                pState.postValue(PlayListState(it))
            }
        }
    }

    fun deleteSelectedPlayList(id: Int) {
        deletePlayListJob = CoroutineScope(Dispatchers.Main).launch {
            pDeletePlayList.postValue(SingleEvent(true))
            deleteOnePlayListUseCase.invoke(id)
        }
    }

    override fun onCleared() {
        playListJob?.cancel()
        deletePlayListJob?.cancel()
        super.onCleared()
    }
}