package com.afrakhteh.musicplayer.views.playMusicActivity


import android.content.Intent
import android.os.Bundle
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
import javax.inject.Inject


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private val viewModel: PlayerViewModel by viewModels { providerFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewModelComponentBuilder.getInstance().injectPlayer(this)

        val path = requireNotNull(intent.extras).getString(Strings.AUDIO_PATH_KEY, "")
        viewModel.getAllAudioWaveData(path)

        initialiseView()
        viewModel.waveListLiveData.observe(this, ::renderList)
        viewModel.frameSizeLiveData.observe(this, ::drawInitialFrame)
    }

    private fun drawInitialFrame(frameSize: Int) {
        binding.playWave.removeAllViews()
        val frameList = ArrayList<Int>()
        for (i in 0..frameSize) {
            frameList.add(1)
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
        //pause or play music
    }

    private fun setVolume(view: View?) {
        //set volume of music
    }

    private fun forwardButton(view: View?) {
        //next music
    }

    private fun rewindButton(view: View?) {
        // previous music
    }

    private fun backButton(view: View?) {
        startActivity(Intent(this, MainActivity::class.java))
    }

}


