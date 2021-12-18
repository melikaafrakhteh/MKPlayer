package com.afrakhteh.musicplayer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.useCase.playList.DeleteMusicFromPlayListUseCase
import com.afrakhteh.musicplayer.model.useCase.playList.GetPlayListWithMusicsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayListMusicsViewModel @Inject constructor(
        private val getPlayListWithMusicsUseCase: GetPlayListWithMusicsUseCase,
        private val deleteMusicFromPlayListUseCase: DeleteMusicFromPlayListUseCase,
        val repository: MusicRepository
) : ViewModel() {

    private var getMusicsOfPlayListJob: Job? = null
    private var removeMusicOfPlayListJob: Job? = null

    private val pAllPlayListMusics = MutableLiveData<List<AllMusicsEntity>>()
    val allPlayListMusic: LiveData<List<AllMusicsEntity>> get() = pAllPlayListMusics

    fun fetchMusicOfThisPlayList(position: Int) {
        getMusicsOfPlayListJob = CoroutineScope(Dispatchers.IO).launch {
            getPlayListWithMusicsUseCase.invoke(position).let {
                pAllPlayListMusics.postValue(it.first().musicList)
            }
        }
    }

    fun deleteOneMusicFromPlayList(item: AllMusicsEntity) {
        removeMusicOfPlayListJob = CoroutineScope(Dispatchers.IO).launch {
            deleteMusicFromPlayListUseCase.invoke(item)
        }
    }

    override fun onCleared() {
        getMusicsOfPlayListJob?.cancel()
        removeMusicOfPlayListJob?.cancel()
        super.onCleared()
    }
}