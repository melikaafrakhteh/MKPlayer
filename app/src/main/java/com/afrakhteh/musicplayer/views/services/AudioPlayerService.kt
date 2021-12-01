package com.afrakhteh.musicplayer.views.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.afrakhteh.musicplayer.constant.AudioActions
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.di.builders.PlayerComponentBuilder
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.audio.AudioRepeatType
import com.afrakhteh.musicplayer.util.SingleEvent
import com.afrakhteh.musicplayer.util.resize
import com.afrakhteh.musicplayer.util.toBitmap
import com.afrakhteh.musicplayer.views.presenter.ArtAlbumPresenter
import com.afrakhteh.musicplayer.views.util.PlayerNotificationHelper
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AudioPlayerService : Service(), Player.Listener, AudioServiceViewInterface {

    @Inject
    lateinit var player: SimpleExoPlayer

    @Inject
    lateinit var presenter: ArtAlbumPresenter

    private var audioListToPlay: List<AudioPrePareToPlay> = emptyList()
    private var shuffledList: List<AudioPrePareToPlay> = emptyList()

    private var currentPosition: Int? = null
    private val audioRepeatType: AudioRepeatType = AudioRepeatType.RepeatAllMusicList

    private lateinit var notificationHelper: PlayerNotificationHelper

    private var isForegroundServiceStarted: Boolean = false
    private var rememberedPosition = 0L


    private val pOnPlayerChangedLiveData = MutableLiveData<Boolean>()
    val onPlayerChangedLiveData: LiveData<Boolean> get() = pOnPlayerChangedLiveData

    private val pOnPlayerChangedDataLiveData = MutableLiveData<SingleEvent<Int>>()
    val onPlayerChangedDataLiveData: LiveData<SingleEvent<Int>> get() = pOnPlayerChangedDataLiveData

    private val pOnPlayBackPositionChanged = MutableLiveData<Long>()
    val onPlayBackPositionChanged: LiveData<Long> get() = pOnPlayBackPositionChanged

    private var imageByteArray: ByteArray? = null

    private var tickerDisposable: Disposable? = null

    inner class AudioBinder : Binder() {
        fun getService(): AudioPlayerService = this@AudioPlayerService
    }

    override fun onBind(intent: Intent?): IBinder {
        return AudioBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        handleIntent(intent)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        PlayerComponentBuilder.getInstance().inject(this)
        presenter.setService(this)

        notificationHelper = PlayerNotificationHelper(
            getSystemService(NOTIFICATION_SERVICE)
                    as NotificationManager, resources
        )
        player.addListener(this)
    }

    override fun onDestroy() {
        player.apply {
            stop()
            release()
            removeListener(this@AudioPlayerService)
        }
        notificationHelper.cancelNotification()
        presenter.onDestroy()
        super.onDestroy()
    }

    fun play(position: Int = 0) {
        if (currentPosition == position) return
        currentPosition = position
        player.stop()
        player.seekTo(0)
        setAudioItemToPlayer(findMusicToPlay(requireNotNull(currentPosition)))
        presenter.getAudioByteArray()
        player.prepare()
        player.play()
        startForeGroundIfNeeded().let {
            if (!it)
                updateNotification()
        }
    }

    private fun findMusicToPlay(currentPosition: Int): AudioPrePareToPlay {
        if (audioRepeatType == AudioRepeatType.ShuffleMusicList) {
            return shuffledList[currentPosition]
        }
        return audioListToPlay[currentPosition]
    }

    private fun setAudioItemToPlayer(audio: AudioPrePareToPlay) {
        val mediaItem: MediaItem = MediaItem.fromUri(
                Uri.fromFile(File(audio.path))
        )
        player.setMediaItem(mediaItem)
    }

    fun pause() {
        rememberedPosition = player.currentPosition
        player.pause()
        updateNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_DETACH)
        }
    }

    fun resume() {
        if (rememberedPosition == 0L) {
            play(currentPosition ?: 0)
            return
        }
        player.seekTo(rememberedPosition)
        player.play()
        updateNotification()
    }

    fun playNext() {
        val nextAudio: Int = findNextPositionToPlay()
        play(nextAudio)
        pOnPlayerChangedDataLiveData.value = SingleEvent(nextAudio)
        updateNotification()
    }

    private fun findNextPositionToPlay(): Int {
        return if (currentPosition == audioListToPlay.size - 1) 0
        else requireNotNull(currentPosition) + 1
    }

    fun playPrevious() {
        val previousAudio: Int = findPreviousPositionToPlay()
        play(previousAudio)
        pOnPlayerChangedDataLiveData.value = SingleEvent(previousAudio)
        updateNotification()
    }

    private fun findPreviousPositionToPlay(): Int {
        return if (currentPosition != 0) requireNotNull(currentPosition) - 1
        else audioListToPlay.size - 1
    }

    fun setAudioList(audioList: List<AudioPrePareToPlay>) {
        audioListToPlay = audioList
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        when (intent.action) {
            AudioActions.ACTION_PLAY -> {
                if (isPlaying()) {
                    pause()
                } else {
                    resume()
                }
            }
            AudioActions.ACTION_NEXT -> {
                playNext()
                pOnPlayerChangedDataLiveData.value = SingleEvent(currentPosition!!)
            }
            AudioActions.ACTION_PREVIOUS -> {
                playPrevious()
                pOnPlayerChangedDataLiveData.value = SingleEvent(currentPosition!!)
            }
        }
    }

    private fun updateNotification() {
        if (audioListToPlay.isEmpty()) return
        notificationHelper.showNotification(
            applicationContext,
            audioListToPlay,
            requireNotNull(currentPosition),
            isPlaying(),
            imageByteArray?.toBitmap()?.resize()
        )
    }

    fun seekToPosition(position: Long) {
        player.seekTo(position)
    }

    fun setVolume(volume: Float) {
        player.volume = volume
    }

    fun getDuration(): Long {
        return player.duration
    }

    private fun startTicking() {
        tickerDisposable = Observable
                .interval(0L, 250L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkCurrentPosition()
                }
    }

    private fun checkCurrentPosition() {
        pOnPlayBackPositionChanged.postValue(player.currentPosition)
    }

    private fun stopTicking() {
        tickerDisposable?.dispose()
        tickerDisposable = null
    }

    private fun startForeGroundIfNeeded(): Boolean {
        if (isForegroundServiceStarted) return false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = notificationHelper.showNotification(
                    this,
                    audioListToPlay,
                    requireNotNull(currentPosition),
                    isPlaying(),
                    imageByteArray?.toBitmap()?.resize()
            )
            startForeground(Numerals.NOTIFICATION_ID, notification)
        }
        isForegroundServiceStarted = true
        return true
    }

    fun isPlaying(): Boolean {
        return player.playWhenReady
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        notificationHelper.showNotification(
            this,
            audioListToPlay,
            requireNotNull(currentPosition),
            playWhenReady,
            imageByteArray?.toBitmap()?.resize()
        )
        pOnPlayerChangedLiveData.postValue(playWhenReady)
        if (playWhenReady) startTicking() else stopTicking()
    }

    override fun getPlayingPosition(): Int? {
        return currentPosition
    }

    override fun getMusicList(): List<AudioPrePareToPlay> {
        return audioListToPlay
    }

    override fun setAudioByteArray(byteArray: ByteArray?) {
        imageByteArray = byteArray
    }


}


