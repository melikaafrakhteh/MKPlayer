package com.afrakhteh.musicplayer.viewModel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.model.entity.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.repository.player.AudioDetailsRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScope
class PlayerViewModel @Inject constructor(
        private val repository: AudioDetailsRepository
) : ViewModel() {

    private lateinit var job: Job
    private val disposable = CompositeDisposable()

    private val pWaveList = MutableLiveData<ArrayList<Int>>()
    val waveListLiveData: LiveData<ArrayList<Int>> get() = pWaveList

    private val pFrameSize = MutableLiveData<Int>()
    val frameSizeLiveData: LiveData<Int> get() = pFrameSize

    private val pAudioList = MutableLiveData<List<AudioPrePareToPlay>>()
    val audioListLiveData: LiveData<List<AudioPrePareToPlay>> get() = pAudioList

    private val pActivePosition = MutableLiveData<Int>()
    val activePositionLiveData: LiveData<Int> get() = pActivePosition

    fun getAllAudioWaveData(path: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val audioWaveData = repository.fetchAudioWaveData(path)
            audioWaveData.frameCountObservable.subscribe {
                pFrameSize.postValue(it)
                disposable.clear()
            }.addTo(disposable)

            audioWaveData.waveDataObservable.subscribeBy(
                    onError = {
                        Log.e("playerViewModel", "player view model error in subscription: $it")
                    },
                    onNext = {
                        pWaveList.postValue(it)
                        disposable.clear()
                    }
            ).addTo(disposable)
        }
    }

    fun getAllMusicList(intent: Intent) {
        pAudioList.value = intent
                .getParcelableArrayListExtra<AudioPrePareToPlay>(Strings.AUDIO_All_MUSIC_LIST_KEY)
                ?.map {
                    it as AudioPrePareToPlay
                }
    }

    fun getMusicActivePosition(intent: Intent) {
        pActivePosition.value = requireNotNull(intent.extras)
                .getInt(Strings.AUDIO_ACTIVE_POSITION__KEY, 0)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable.clear()
    }
}