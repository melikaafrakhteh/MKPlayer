package com.afrakhteh.musicplayer.model.repository.player


import io.reactivex.subjects.PublishSubject

class AudioDetailsRepositoryImpl : AudioDetailsRepository {

    private val audioDataSource: PublishSubject<ArrayList<Int>> = PublishSubject.create()

    /* override suspend fun fetchAudioWaveData(path: String): Observable<ArrayList<Int>> {
     val data = AudioWaveDataSource(AudioDecoderImpl(MediaExtractor()))
          data.decodeAudio(path) { list ->
              audioDataSource.onNext(list)
          }
         return audioDataSource
     }*/


}