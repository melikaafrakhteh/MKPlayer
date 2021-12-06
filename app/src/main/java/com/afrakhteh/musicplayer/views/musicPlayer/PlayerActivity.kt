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
import com.afrakhteh.musicplayer.constant.Lists
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

    private var isUserScrolling: Boolean = false
    private var volumePosition = 1


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
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioPlayerService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewModelComponentBuilder.getInstance().inject(this)

        viewModel.apply {
            getMusicArtPicture()
            getAllAudioWaveData()
            getAllMusicList(intent)
            getMusicActivePosition(intent)
        }

        binding.playWaveRecyclerView.adapter = PlayerWaveItemsAdapter(
                getScreenSize().y, mutableListOf(WaveItemModel())
        )

        val startPlayingMusicIntent = Intent(this, AudioPlayerService::class.java)
        startService(startPlayingMusicIntent)
        bindService(startPlayingMusicIntent, connectToService, BIND_AUTO_CREATE)

        viewModel.apply {
            waveListLiveData.observe(this@PlayerActivity, ::renderList)
            frameSizeLiveData.observe(this@PlayerActivity, ::drawInitialFrame)
            artPicture.observe(this@PlayerActivity, ::observeArtPicture)
        }

        initialiseView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialiseView() {
        binding.playWaveRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isUserScrolling.not()) return
                audioPlayerService?.getDuration()?.let { duration ->
                    audioPlayerService?.seekToPosition(getTargetPosition(duration))
                }
            }
        })
        binding.playWaveRecyclerView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> isUserScrolling = true
                MotionEvent.ACTION_UP -> isUserScrolling = false
                MotionEvent.ACTION_DOWN -> isUserScrolling = false
            }
            return@setOnTouchListener false
        }

        binding.playVolumeProgressBar.setOnTouchListener(::onVolumeTouchListener)

        isFavorite()
        buttonClicks()
    }

    private fun isFavorite() {
        val musicPath = viewModel.activePositionLiveData.value?.let {
            viewModel.audioListLiveData.value?.get(it)?.path
        }
        viewModel.getAllFaveList().observe(this) { liked ->
            liked.forEach {
                if (it.path == musicPath) {
                    binding.playFaveToggleButton.isChecked = true
                    return@forEach
                }
            }
        }
    }

    private fun onVolumeTouchListener(view: View?, motionEvent: MotionEvent?): Boolean {
        val percents = calculateVolumeActiveProgress(requireNotNull(motionEvent))
        when (motionEvent.action) {
            MotionEvent.ACTION_MOVE -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    binding.playVolumeProgressBar.setProgress(percents.toInt(), true)
                } else {
                    binding.playVolumeProgressBar.progress = percents.toInt()
                }
                audioPlayerService?.setVolume(percents / 100)
                changeVolumeIcon(percents / 100)
            }
        }
        return true
    }

    private fun changeVolumeIcon(percent: Float) {
        when (percent) {
            in -0.0f..0.01f -> {
                binding.playVolume.setImageResource(R.drawable.volume_mute)
            }
            in 0.02f..0.29f -> {
                binding.playVolume.setImageResource(R.drawable.ic_volume_down)
            }
            in 0.30f..0.68f -> {
                binding.playVolume.setImageResource(R.drawable.ic_volume_mid)
            }
            in 0.70f..1.20f -> {
                binding.playVolume.setImageResource(R.drawable.ic_volume_up)
            }
        }
    }

    private fun findTopVolumeProgressBarOnTheScreen(): Int {
        val locationIntArray = IntArray(2)
        binding.playVolumeProgressBar.getLocationOnScreen(locationIntArray)
        return locationIntArray[1]
    }

    private fun calculateVolumeActiveProgress(motionEvent: MotionEvent): Float {
        val volumePBBottom = binding.playVolumeProgressBar.height
        val userPointY = motionEvent.rawY

        val diffY = userPointY - findTopVolumeProgressBarOnTheScreen()
        val activeY = volumePBBottom - diffY

        return (activeY / volumePBBottom) * 100
    }

    private fun getTargetPosition(duration: Long): Long {
        val currentScrollPosition = binding.playWaveRecyclerView.computeVerticalScrollOffset()
        val scrollRange = binding.playWaveRecyclerView.computeVerticalScrollRange()
        return (duration * currentScrollPosition) / scrollRange
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
        setAudioDuration()
    }

    private fun setAudioDuration() {
        val duration = requireNotNull(audioPlayerService?.getDuration())
        if (checkDurationValidation(duration)) {
            val durationSet = duration / 1000
            val min = ((durationSet / 60)).toInt()
            val sec = (durationSet % 60).toInt()
            setDurationTimer(min, sec)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDurationTimer(min: Int, sec: Int) {
        binding.playMusicDuration.text =
                "${String.format("%02d", min)}:${String.format("%02d", sec)}"
    }

    private fun checkDurationValidation(duration: Long): Boolean {
        return duration >= 0
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
        val adapter = binding.playWaveRecyclerView.adapter as PlayerWaveItemsAdapter
        val frameList = ArrayList<WaveItemModel>()

        arrayList.ifNotHandled {
            for (i in 0 until it.size) {
                frameList.addAll(listOf(WaveModel(it[i], true)))
            }
        }
        adapter.submitList(frameList)
    }

    private fun onPlayBackPositionChanged(currentPosition: Long) {
        setAudioDuration()
        if (isUserScrolling) return
        if (binding.playWaveRecyclerView.layoutParams == null) return
        try {
            val currentScrollPosition = binding.playWaveRecyclerView.computeVerticalScrollOffset()
            val scrollRange = binding.playWaveRecyclerView.computeVerticalScrollRange()
            val scrollPosition =
                    (currentPosition * scrollRange) / audioPlayerService?.getDuration()!!

            binding.playWaveRecyclerView.smoothScrollBy(
                    0, ((scrollPosition - currentScrollPosition).toInt()),
                    LinearInterpolator(),
                    240
            )

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
        binding.playFaveToggleButton.setOnClickListener(::faveItem)
    }

    private fun faveItem(view: View?) {
        val currentMusicData = viewModel.audioListLiveData.value?.get(viewModel.activePositionLiveData.value!!)

        if (binding.playFaveToggleButton.isChecked) {
            viewModel.addMusicToFavoriteList(requireNotNull(currentMusicData))
            Log.d("player", "add to fave")
            binding.playFaveToggleButton.isChecked = true
        } else {
            viewModel.removeMusicFromFavoriteList(requireNotNull(currentMusicData?.path))
            Log.d("player", "remove fave")
            binding.playFaveToggleButton.isChecked = false
        }
    }

    private fun handlePlayOrPause(view: View?) {
        if (requireNotNull(audioPlayerService).isPlaying()) {
            audioPlayerService?.pause()
        } else {
            audioPlayerService?.resume()
        }
    }

    private fun setVolume(view: View?) {
        volumePosition++
        if (volumePosition > 3) {
            volumePosition = 0
        }
        binding.playVolume.setImageResource(Lists.VOLUME_IMAGE_LIST[volumePosition])
        binding.playVolumeProgressBar.progress = Lists.VOLUME_PB_LIST[volumePosition]
        audioPlayerService?.setVolume(Lists.VOLUME_LIST[volumePosition])
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