package com.afrakhteh.musicplayer.views.playMusicActivity


import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.databinding.ActivityPlayerBinding
import com.afrakhteh.musicplayer.di.builders.ViewModelComponentBuilder
import com.afrakhteh.musicplayer.util.getScreenSize
import com.afrakhteh.musicplayer.viewModel.PlayerViewModel
import com.afrakhteh.musicplayer.views.mainActivity.MainActivity
import com.afrakhteh.musicplayer.views.services.AudioPlayerService
import javax.inject.Inject


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private val viewModel: PlayerViewModel by viewModels { providerFactory }

    private var audioPlayerService: AudioPlayerService? = null

    private val connectToService = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            audioPlayerService = (service as AudioPlayerService.AudioBinder).getService()
            requireNotNull(audioPlayerService)
                    .onPlayerChangedLiveData.observe(this@PlayerActivity, ::onPlayedChanged)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioPlayerService = null
        }
    }

    private fun onPlayedChanged(isPlaying: Boolean?) {
        if (isPlaying != null) {
            binding.playToggleBtn.isChecked = isPlaying
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewModelComponentBuilder.getInstance().injectPlayer(this)

        val path = requireNotNull(intent.extras).getString(Strings.AUDIO_PATH_KEY, "")!!
        viewModel.getAllAudioWaveData(path)

        initialiseView()

        viewModel.waveListLiveData.observe(this, ::renderList)
        viewModel.frameSizeLiveData.observe(this, ::drawInitialFrame)

        bindService(Intent(this,
                AudioPlayerService::class.java), connectToService, BIND_AUTO_CREATE)
    }

    private fun drawInitialFrame(frameSize: Int) {
        binding.playWave.removeAllViews()
        val frameList = ArrayList<Int>()
        for (i in 0..frameSize) {
            frameList.add(2)
        }
        binding.playWave.showWaves(frameList, getScreenSize().y)
    }

    private fun renderList(arrayList: ArrayList<Int>) {
        binding.playWave.removeAllViews()
        binding.playWave.showWaves(arrayList, getScreenSize().y)
    }

    private fun initialiseView() {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val musicName = requireNotNull(intent.extras).getString(Strings.AUDIO_NAME_KEY, "")
        val musicArtistName = requireNotNull(intent.extras).getString(Strings.AUDIO_ARTIST_KEY, "")

        binding.playMusicNameTv.text = musicName
        binding.playMusicArtistTv.text = musicArtistName

        buttonClicks()
    }

    private fun buttonClicks() {
        binding.playBackIv.setOnClickListener(::backButton)
        binding.playRewindBtnIv.setOnClickListener(::rewindButton)
        binding.playForwardBtnIv.setOnClickListener(::forwardButton)
        binding.playVolume.setOnClickListener(::setVolume)
        binding.playToggleBtn.setOnClickListener(::handlePlayOrPause)
    }

    private fun handlePlayOrPause(view: View?) {
        if (requireNotNull(audioPlayerService).isPlaying()) {
            audioPlayerService?.pause()
        } else {
            audioPlayerService?.play()
        }
    }

    private fun setVolume(view: View?) {
        //set volume of music
    }

    private fun forwardButton(view: View?) {
        audioPlayerService?.playNext()
    }

    private fun rewindButton(view: View?) {
        audioPlayerService?.playPrevious()
    }

    private fun backButton(view: View?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        unbindService(connectToService)
        super.onDestroy()
    }
}


