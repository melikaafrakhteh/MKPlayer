package com.afrakhteh.musicplayer.model.repository.player

import android.media.MediaExtractor
import com.afrakhteh.musicplayer.di.scopes.RepoScope
import com.afrakhteh.musicplayer.model.dataSource.AudioWaveReadable
import com.afrakhteh.musicplayer.model.dataSource.decoding.AudioDecoderImpl
import com.afrakhteh.musicplayer.model.entity.AudioWaveDataRequest
import com.afrakhteh.musicplayer.model.entity.AudioWaveDataResult
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@RepoScope
class AudioDetailsRepositoryImpl
@Inject constructor() : AudioDetailsRepository {

    private val audioWaveDataObservable: PublishSubject<ArrayList<Int>> = PublishSubject.create()
    private val audioFrameCountedObservable: BehaviorSubject<Int> = BehaviorSubject.create()

    override suspend fun fetchAudioWaveData(
            path: String
    ): AudioWaveDataResult {
        val audioWaveReadable = AudioWaveReadable(AudioDecoderImpl(MediaExtractor()))
        audioWaveReadable.read(
                AudioWaveDataRequest(
                        path,
                        { list ->
                            audioWaveDataObservable.onNext(list)
                        },
                        { frameAmpsSize ->
                            audioFrameCountedObservable.onNext(frameAmpsSize)
                        }
                )
        )
        return AudioWaveDataResult(audioWaveDataObservable, audioFrameCountedObservable)
    }
}