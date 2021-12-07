package com.afrakhteh.musicplayer.viewModel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.di.scopes.ViewModelScope
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.db.FavoriteEntity
import com.afrakhteh.musicplayer.model.repository.musics.MusicRepository
import com.afrakhteh.musicplayer.model.repository.player.AudioDetailsRepository
import com.afrakhteh.musicplayer.model.useCase.AddToFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.DeleteFromFaveUseCase
import com.afrakhteh.musicplayer.model.useCase.IsMusicLikedUseCase
import com.afrakhteh.musicplayer.util.SingleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.*
import javax.inject.Inject

@ViewModelScope
class PlayerViewModel @Inject constructor(
        private val repository: AudioDetailsRepository,
        private val musicRepository: MusicRepository,
        private val addUseCases: AddToFaveUseCase,
        private val removeUseCase: DeleteFromFaveUseCase,
        private val isMusicLikedUseCase: IsMusicLikedUseCase
) : ViewModel() {

    private lateinit var job: Job
    private lateinit var job2: Job
    private val disposable = CompositeDisposable()

    private val pWaveList = MutableLiveData<SingleEvent<ArrayList<Int>>>()
    val waveListLiveData: LiveData<SingleEvent<ArrayList<Int>>> get() = pWaveList

    private val pFrameSize = MutableLiveData<SingleEvent<Int>>()
    val frameSizeLiveData: LiveData<SingleEvent<Int>> get() = pFrameSize

    private val pAudioList = MutableLiveData<List<AudioPrePareToPlay>>()
    val audioListLiveData: LiveData<List<AudioPrePareToPlay>> get() = pAudioList

    private val pActivePosition = MutableLiveData<Int>()
    val activePositionLiveData: LiveData<Int> get() = pActivePosition

    private val pArtPicture = MutableLiveData<ByteArray?>()
    val artPicture: LiveData<ByteArray?> get() = pArtPicture

    private val pPlayingPosition = MutableLiveData<Int>()
    val playingPosition: LiveData<Int> get() = pPlayingPosition

    fun getAllAudioWaveData() {
        job = CoroutineScope(Dispatchers.Main).launch {
            if (activePositionLiveData.value == null) return@launch
            val audioWaveData = repository.fetchAudioWaveData(
                    audioListLiveData.value!![activePositionLiveData.value!!].path
            )
            withContext(Dispatchers.IO) {
                audioWaveData.frameCountObservable
                        .subscribe {
                            pFrameSize.postValue(SingleEvent(it))
                            disposable.clear()
                        }.addTo(disposable)

                audioWaveData.waveDataObservable
                        .subscribeBy(
                                onError = {
                                    Log.e("playerViewModel", "player view model error in subscription: $it")
                                },
                                onNext = {
                                    pWaveList.postValue(SingleEvent(it))
                                    disposable.clear()
                                }
                        ).addTo(disposable)
            }

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
                    if (it == -1) return@let
                    pActivePosition.value = it
                    pPlayingPosition.value = it
                }
    }

    fun changeMusicActivePosition(position: Int) {
        pActivePosition.value = position
        getMusicArtPicture()
        getAllAudioWaveData()
    }

    fun getMusicArtPicture() {
        job2 = CoroutineScope(Dispatchers.Main).launch {
            if (activePositionLiveData.value == null) return@launch
            pAudioList.value?.get(activePositionLiveData.value!!).let {
                pArtPicture.postValue(musicRepository.getMusicArtPicture(it!!.path))
            }
        }
    }

    suspend fun addMusicToFavoriteList(item: AudioPrePareToPlay) {
        addUseCases.invoke(FavoriteEntity(item.id, item.path))
    }

    suspend fun removeMusicFromFavoriteList(path: String) {
        removeUseCase.invoke(path)
    }

    suspend fun isMusicLiked(path: String): Boolean {
        return isMusicLikedUseCase.invoke(path)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        job2.cancel()
        disposable.clear()
    }
}