package com.afrakhteh.musicplayer.views.playMusicActivity


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.afrakhteh.musicplayer.databinding.ActivityPlayerBinding
import com.afrakhteh.musicplayer.util.getScreenSize


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseView()
    }

    private fun initialiseView() {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var list: MutableList<Int> = ArrayList()
        /*  requireNotNull(intent.extras).getIntArray("data")?.forEach {
              list.add(it)
          }
          binding.playWave.showWaves(list, getScreenSize().y)*/

        //test :To ensure the correct shape
        list = arrayListOf(1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3)
        binding.playWave.showWavesBeforeProcessing(list, getScreenSize().y)

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
        //back to main activity
    }


}


