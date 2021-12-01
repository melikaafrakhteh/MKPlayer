package com.afrakhteh.musicplayer.views.musicPlayer


import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.ActivityPlayerBinding
import com.afrakhteh.musicplayer.di.builders.ViewModelComponentBuilder
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.wave.WaveItemModel
import com.afrakhteh.musicplayer.model.entity.wave.WaveModel
import com.afrakhteh.musicplayer.util.SingleEvent
import com.afrakhteh.musicplayer.util.getScreenSize
import com.afrakhteh.musicplayer.util.resize
import com.afrakhteh.musicplayer.util.toBitmap
import com.afrakhteh.musicplayer.viewModel.PlayerViewModel
import com.afrakhteh.musicplayer.views.musicPlayer.adapter.PlayerWaveItemsAdapter
import com.afrakhteh.musicplayer.views.services.AudioPlayerService
import javax.inject.Inject


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private val viewModel: PlayerViewModel by viewModels { providerFactory }

    private var audioPlayerService: AudioPlayerService? = null

    private var frames: ArrayList<WaveItemModel> = ArrayList()

    private var isUserScrolling: Boolean = false


    private val connectToService = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            audioPlayerService = (service as AudioPlayerService.AudioBinder).getService()

            requireNotNull(audioPlayerService).apply {
                onPlayerChangedLiveData.observe(this@PlayerActivity, ::onPlayedChanged)
                onPlayerChangedDataLiveData.observe(this@PlayerActivity, ::onChangedUiData)
                onPlayBackPositionChanged.observe(this@PlayerActivity, ::onPlayBackPositionChanged)
            }
            viewModel.apply {
                audioListLiveData.observe(this@PlayerActivity, ::getListToPlay)
                activePositionLiveData.observe(this@PlayerActivity, ::getActiveAudioPosition)
                playingPosition.observe(this@PlayerActivity, ::observePlayingPosition)
            }
            findAudioDuration()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioPlayerService = null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewModelComponentBuilder.getInstance().injectPlayer(this)

        viewModel.apply {
            getMusicArtPicture()
            getAllAudioWaveData()
            getAllMusicList(intent)
            getMusicActivePosition(intent)
        }

        binding.playWaveRecyclerView.adapter = PlayerWaveItemsAdapter(getScreenSize().y)

        val startPlayingMusicIntent = Intent(this, AudioPlayerService::class.java)
        startService(startPlayingMusicIntent)
        bindService(startPlayingMusicIntent, connectToService, BIND_AUTO_CREATE)

        viewModel.apply {
            waveListLiveData.observe(this@PlayerActivity, ::renderList)
            frameSizeLiveData.observe(this@PlayerActivity, ::drawInitialFrame)
            artPicture.observe(this@PlayerActivity, ::observeArtPicture)
        }

        buttonClicks()

        binding.playWaveRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        })
        binding.playWaveRecyclerView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> isUserScrolling = true
                MotionEvent.ACTION_UP -> isUserScrolling = false
                MotionEvent.ACTION_DOWN -> isUserScrolling = false
            }
            return@setOnTouchListener true
        }

    }

    private fun getActiveAudioPosition(position: Int?) {
        if (position == null) return
        val musicPrePareToPlay = viewModel.audioListLiveData.value!![position]
        binding.playMusicNameTv.text = musicPrePareToPlay.musicName
        binding.playMusicArtistTv.text = musicPrePareToPlay.musicArtist
    }

    private fun getListToPlay(list: List<AudioPrePareToPlay>?) {
        audioPlayerService?.setAudioList(list ?: emptyList())
    }

    private fun onPlayedChanged(isPlaying: Boolean?) {
        if (isPlaying != null) {
            binding.playToggleBtn.isChecked = isPlaying
        }
    }

    private fun observePlayingPosition(position: Int?) {
        audioPlayerService?.play(position ?: 0)
    }

    private fun observeArtPicture(bytes: ByteArray?) {
        if (bytes == null) {
            binding.playMusicCoverIv.setImageResource(R.drawable.dog)
            return
        }
        val image = bytes.toBitmap().resize()
        binding.playMusicCoverIv.setImageBitmap(image)
    }

    private fun onChangedUiData(position: SingleEvent<Int>) {
        position.ifNotHandled {
            viewModel.changeMusicActivePosition(it)
        }

    }

    private fun findAudioDuration() {
        val duration = requireNotNull(audioPlayerService?.getDuration()) / 1000
        Log.d("player", "duration: ${audioPlayerService?.getDuration()}")
        if (duration > 0L) {
            val min = ((duration / 60) % 60).toInt()
            val sec = (duration % 60).toInt()
            setDurationTimer(min, sec)
        }
    }

    private fun setDurationTimer(min: Int, sec: Int) {
        binding.playMusicDuration.text = "$min:${String.format("%02d", sec)}"
    }


    private fun drawInitialFrame(frameSize: SingleEvent<Int>) {
        val adapter = binding.playWaveRecyclerView.adapter as PlayerWaveItemsAdapter
        val frameList = ArrayList<WaveItemModel>()
        adapter.submitList(frameList)

        frameSize.ifNotHandled {
            for (i in 0..it) {
                frameList.add(WaveModel(1, true))
            }
        }
        adapter.submitList(frameList)
    }

    private fun renderList(arrayList: SingleEvent<ArrayList<Int>>) {
        binding.playWaveRecyclerView.removeAllViews()
        val adapter = binding.playWaveRecyclerView.adapter as PlayerWaveItemsAdapter
        val frameList = ArrayList<WaveItemModel>()
        arrayList.ifNotHandled {
            for (i in 0 until it.size) {
                frameList.addAll(listOf(WaveModel(it[i], true)))
            }
        }
        frames.addAll(frameList)
        adapter.submitList(frameList)

    }

    private fun onPlayBackPositionChanged(currentPosition: Long) {
        if (binding.playWaveRecyclerView.layoutParams == null) return
        try {
            val currentScrollPosition = binding.playWaveRecyclerView.computeVerticalScrollOffset()
            val scrollRange = binding.playWaveRecyclerView.computeVerticalScrollRange()
            val scrollPosition = (currentPosition * scrollRange) / audioPlayerService?.getDuration()!!

            binding.playWaveRecyclerView.smoothScrollBy(
                    0, ((scrollPosition - currentScrollPosition).toInt()),
                    LinearInterpolator(),
                    240)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun buttonClicks() {
        binding.playBackIv.setOnClickListener(::backButton)
        binding.playNextBtnIv.setOnClickListener(::previousButton)
        binding.playPreviousBtnIv.setOnClickListener(::nextButton)
        binding.playVolume.setOnClickListener(::setVolume)
        binding.playToggleBtn.setOnClickListener(::handlePlayOrPause)
    }

    private fun handlePlayOrPause(view: View?) {
        if (requireNotNull(audioPlayerService).isPlaying()) {
            audioPlayerService?.pause()
        } else {
            audioPlayerService?.resume()
        }
    }

    private fun setVolume(view: View?) {
        //set volume of music
    }

    private fun nextButton(view: View?) {
        audioPlayerService?.playNext()
    }

    private fun previousButton(view: View?) {
        audioPlayerService?.playPrevious()
    }

    private fun backButton(view: View?) {
        finish()
    }

    override fun onDestroy() {
        unbindService(connectToService)
        super.onDestroy()
    }

}


