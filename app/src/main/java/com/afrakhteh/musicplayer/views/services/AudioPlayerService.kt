package com.afrakhteh.musicplayer.views.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.afrakhteh.musicplayer.constant.AudioActions
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.di.builders.PlayerComponentBuilder
import com.afrakhteh.musicplayer.model.entity.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.AudioRepeatType
import com.afrakhteh.musicplayer.views.util.PlayerNotificationHelper
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class AudioPlayerService : Service(), Player.Listener {

    @Inject
    lateinit var player: SimpleExoPlayer

    private var audioListToPlay: List<AudioPrePareToPlay> = emptyList()
    private var shuffledList: List<AudioPrePareToPlay> = emptyList()

    private var currentPosition: Int? = null
    private val audioRepeatType: AudioRepeatType = AudioRepeatType.RepeatAllMusicList

    private lateinit var notificationHelper: PlayerNotificationHelper

    private var isForegroundServiceStarted: Boolean = false
    private var rememberedPosition = 0L

    private val pOnPlayerChangedLiveData = MutableLiveData<Boolean>()
    val onPlayerChangedLiveData: LiveData<Boolean> get() = pOnPlayerChangedLiveData

    private val pOnPlayerChangedDataLiveData = MutableLiveData<Int>()
    val onPlayerChangedDataLiveData: LiveData<Int> get() = pOnPlayerChangedDataLiveData

    private lateinit var job: Job

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
        notificationHelper = PlayerNotificationHelper(
                getSystemService(NOTIFICATION_SERVICE)
                        as NotificationManager)
        player.addListener(this)

    }

    override fun onDestroy() {
        player.apply {
            stop()
            release()
            removeListener(this@AudioPlayerService)
        }
        notificationHelper.cancelNotification()
        job.cancel()
        super.onDestroy()
    }

    fun play(position: Int = 0) {
        if (currentPosition == position) return
        currentPosition = position
        player.stop()
        player.seekTo(0)
        setAudioItemToPlayer(findMusicToPlay(requireNotNull(currentPosition)))
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
        pOnPlayerChangedDataLiveData.value = nextAudio
        updateNotification()
    }

    private fun findNextPositionToPlay(): Int {
        return if (currentPosition == audioListToPlay.size - 1) 0
        else requireNotNull(currentPosition) + 1
    }

    fun playPrevious() {
        val previousAudio: Int = findPreviousPositionToPlay()
        play(previousAudio)
        pOnPlayerChangedDataLiveData.value = previousAudio
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
                pOnPlayerChangedDataLiveData.value = currentPosition!!
            }
            AudioActions.ACTION_PREVIOUS -> {
                playPrevious()
                pOnPlayerChangedDataLiveData.value = currentPosition!!
            }
        }
    }

    private fun updateNotification() {
        if (audioListToPlay.isEmpty()) return
        notificationHelper.showNotification(
                applicationContext,
                audioListToPlay[requireNotNull(currentPosition)], isPlaying(),
                //showAlbumArt(getMusicAlbumArt())
        )
    }

    fun getVolume(): Float {
        return player.volume
    }

    fun setVolume(volume: Float) {
        player.volume = volume
    }

    private fun startForeGroundIfNeeded(): Boolean {
        if (isForegroundServiceStarted) return false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = notificationHelper.showNotification(
                    this,
                    findMusicToPlay(requireNotNull(currentPosition)),
                    isPlaying(),
                    // showAlbumArt(getMusicAlbumArt())
            )
            startForeground(Numerals.NOTIFICATION_ID, notification)
        }
        isForegroundServiceStarted = true
        return true
    }

    fun isPlaying(): Boolean {
        return player.playWhenReady
    }

    fun getMusicAlbumArt(): ByteArray {
        var albumByte: ByteArray? = null
        job = CoroutineScope(Dispatchers.Main).launch {
            if (currentPosition == null) return@launch
            //   else albumByte = repository.getMusicArtPicture(findMusicToPlay(currentPosition!!).path)
        }
        return albumByte!!
    }

    fun showAlbumArt(byte: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byte, 0, byte.size)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)

        notificationHelper.showNotification(
                this,
                findMusicToPlay(requireNotNull(currentPosition)),
                playWhenReady,
                //  showAlbumArt(getMusicAlbumArt())
        )

        pOnPlayerChangedLiveData.postValue(playWhenReady)
    }

}


