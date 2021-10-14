package com.afrakhteh.musicplayer.model.repository.player

import android.media.MediaExtractor
import com.afrakhteh.musicplayer.di.scopes.RepoScope
import com.afrakhteh.musicplayer.model.dataSource.AudioWaveDataSource
import com.afrakhteh.musicplayer.model.dataSource.decoding.AudioDecoderImpl
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@RepoScope
class AudioDetailsRepositoryImpl
@Inject constructor() : AudioDetailsRepository {

    private val audioWaveDataObservable: PublishSubject<ArrayList<Int>> = PublishSubject.create()

    override suspend fun fetchAudioWaveData(
            path: String
    ): Observable<ArrayList<Int>> {
        val data = AudioWaveDataSource(AudioDecoderImpl(MediaExtractor()))
        data.decodeAudio(path) { list ->
            audioWaveDataObservable.onNext(list)
        }
        return audioWaveDataObservable
    }
}