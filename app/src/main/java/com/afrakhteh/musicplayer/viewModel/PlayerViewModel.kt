package com.afrakhteh.musicplayer.viewModel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.model.entity.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.repository.player.AudioDetailsRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScope
class PlayerViewModel @Inject constructor(
        private val repository: AudioDetailsRepository,
        private val musicRepository: MusicRepository
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

    private val pArtPicture = MutableLiveData<ByteArray?>()
    val artPicture: LiveData<ByteArray?> get() = pArtPicture

    fun getAllAudioWaveData(path: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val audioWaveData = repository.fetchAudioWaveData(path)
            audioWaveData.frameCountObservable
                    .subscribeOn(Schedulers.computation())
                    .subscribe {
                        pFrameSize.postValue(it)
                        disposable.clear()
                    }.addTo(disposable)

            audioWaveData.waveDataObservable
                    .subscribeOn(Schedulers.computation())
                    .subscribeBy(
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
        intent.getParcelableArrayListExtra<AudioPrePareToPlay>(Strings.AUDIO_All_MUSIC_LIST_KEY)
                .let { list ->
                    pAudioList.value = list?.map {
                        it as AudioPrePareToPlay
                    }
                }
    }

    fun getMusicActivePosition(intent: Intent) {
        requireNotNull(intent.extras)
                .getInt(Strings.AUDIO_ACTIVE_POSITION__KEY, -1).let {
                    if (it != -1)
                        pActivePosition.value = it
                }
    }

    fun getMusicArtPicture() {
        viewModelScope.launch {
            if (activePositionLiveData.value == null) return@launch
            pAudioList.value?.get(activePositionLiveData.value!!).let {
                pArtPicture.value = musicRepository.getMusicArtPicture(it!!.path)
                Log.d("vm", it.path)
                Log.d("vm", "${pArtPicture.value}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable.clear()
    }
}