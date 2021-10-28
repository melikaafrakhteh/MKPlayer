package com.afrakhteh.musicplayer.views.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.afrakhteh.musicplayer.constant.AudioActions
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.di.builders.PlayerComponentBuilder
import com.afrakhteh.musicplayer.model.entity.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.AudioRepeatType
import com.afrakhteh.musicplayer.util.NotificationHelper
import com.afrakhteh.musicplayer.views.playMusicActivity.state.AudioState
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import javax.inject.Inject

class AudioPlayerService : Service() {

    @Inject
    var player: SimpleExoPlayer? = null

    private var audioListToPlay: List<AudioPrePareToPlay> = emptyList()
    private val binder by lazy { AudioBinder() }

    private var isPlaying: Boolean = false
    private var playerPosition: Int = 0
    private var isShuffleModeOn: Boolean = false
    private var random = (audioListToPlay.indices).random()
    var state = 0

    private var mExoPlayerIsStopped = true
    private var mExoSongStateCallback: OnMusicStateCallback? = null

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        handleIntent(intent)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        PlayerComponentBuilder.getInstance().injectPlayer(player!!)
        requireNotNull(player).run {
            addListener(ExoPlayerListener())
            // addMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    override fun onDestroy() {
        mExoPlayerIsStopped = true
        hideNotification()
        releasePlayer()
        super.onDestroy()
    }

    private fun releasePlayer() {
        requireNotNull(player).run {
            playWhenReady = false
            stop()
            release()
            null
        }
    }

    /* @SuppressLint("RestrictedApi")
     private fun prepareMediaPlayer() {
        val audioAttributes = AudioAttributes.Builder()
                 .setContentType(C.CONTENT_TYPE_MUSIC)
                 .setUsage(C.USAGE_MEDIA)
                 .build()

         val mediaItem: MediaItem = MediaItem.Builder()
        //         .setUri()
                 .setMediaId()
                 .build()

         val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(this)

         val trackSelector = DefaultTrackSelector(this)

         player = SimpleExoPlayer.Builder(this)
                 .setMediaSourceFactory(mediaSourceFactory)
                 .setAudioAttributes(audioAttributes, true)
                 .setTrackSelector(trackSelector)
                 .build()

         requireNotNull(player).run {
             addListener(ExoPlayerListener())
             addMediaItem(mediaItem)
             prepare()
             playWhenReady = true
         }

     }*/

    fun play(musicPosition: Int = playerPosition) {
        showNotification()
        isPlaying = true
        mExoPlayerIsStopped = false
        val differentMusic = musicPosition != playerPosition
        if (!differentMusic) {
            requireNotNull(player).play()
        }
    }

    fun pause() {
        isPlaying = false
        requireNotNull(player).pause()
        requireNotNull(player).playWhenReady = true
    }

    fun playNextMusicOfList() {
        if (!isShuffleModeOn) {
            play(playerPosition++)
        } else {
            play(random)
        }
    }

    fun playPreviousMusicOfList() {
        if (checkPlayerPosition()) {
            if (!isShuffleModeOn) {
                play(playerPosition--)
            } else {
                play(random)
            }
        }
    }

    private fun checkPlayerPosition(): Boolean {
        return audioListToPlay.isNotEmpty() && playerPosition != 0
    }

    fun changeRepeatType(repeat: AudioRepeatType) {
        when (repeat) {
            is AudioRepeatType.NoRepeat -> {
                setRepeatMode(Numerals.NO_REPEAT_MODE)
            }
            is AudioRepeatType.RepeatAllMusicList -> {
                setRepeatMode(Numerals.ALL_MUSIC_REPEAT_MODE)
            }
            is AudioRepeatType.RepeatOneMusic -> {
                setRepeatMode(Numerals.ONE_MUSIC_REPEAT_MODE)
            }
            is AudioRepeatType.ShuffleMusicList -> {
                isShuffleModeOn = requireNotNull(player).shuffleModeEnabled
            }
        }
    }

    private fun setRepeatMode(repeat: Int) {
        when (repeat) {
            Numerals.NO_REPEAT_MODE -> {
                requireNotNull(player).repeatMode = Player.REPEAT_MODE_OFF
            }
            Numerals.ALL_MUSIC_REPEAT_MODE -> {
                requireNotNull(player).repeatMode = Player.REPEAT_MODE_ALL
            }
            Numerals.ONE_MUSIC_REPEAT_MODE -> {
                requireNotNull(player).repeatMode = Player.REPEAT_MODE_ONE
            }
        }
    }

    fun setAudioList(audioList: List<AudioPrePareToPlay>) {
        if (audioListToPlay.isEmpty()) return
        audioListToPlay = audioList
    }

    private fun showNotification() {
        startForeground(
                Numerals.NOTIFICATION_ID,
                NotificationHelper(this).getNotification()
        )
    }

    private fun hideNotification() {
        pause()
        stopForeground(true)
        stopSelf()
        NotificationHelper(this).cancelNotification()
    }

    private fun handleIntent(intent: Intent?) {
        requireNotNull(intent).extras?.run {
            when (intent.action) {
                AudioActions.ACTION_PLAY -> {
                    if (isPlaying) {
                        pause()
                    } else {
                        play()
                    }
                }
                AudioActions.ACTION_NEXT -> playNextMusicOfList()
                AudioActions.ACTION_PREVIOUS -> playPreviousMusicOfList()
            }
        }
    }

    fun setCurrentSongState() {
        if (player == null) {
            state = if (mExoPlayerIsStopped) AudioState.STOPPED_STATE
            else AudioState.NONE_STATE
            requireNotNull(mExoSongStateCallback).onPlaybackStatusChanged(state)
        }
        state = when (player?.playbackState) {
            Player.STATE_IDLE -> AudioState.PAUSE_STATE
            Player.STATE_BUFFERING -> AudioState.BUFFERING_STATE
            Player.STATE_READY -> {
                if (player?.playWhenReady == true) AudioState.PLAY_STATE
                else AudioState.PAUSE_STATE
            }
            Player.STATE_ENDED -> AudioState.STOPPED_STATE
            else -> AudioState.NONE_STATE
        }
        requireNotNull(mExoSongStateCallback).onPlaybackStatusChanged(state)
    }

    inner class AudioBinder : Binder() {
        fun getService(): AudioPlayerService = this@AudioPlayerService
    }
}