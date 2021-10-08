package com.afrakhteh.musicplayer.views.playMusicActivity


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.databinding.ActivityPlayerBinding
import com.afrakhteh.musicplayer.util.getScreenSize
import com.afrakhteh.musicplayer.util.toPx
import com.afrakhteh.musicplayer.views.playMusicActivity.customs.line.VerticalLine


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    //  private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseView()
        //    initialiseViewModel()

        val path = requireNotNull(intent.extras).getString(Strings.AUDIO_PATH_KEY, "")
        //  viewModel.getAllAudioWaveData(path)
    }

    private fun initialiseViewModel() {
        //   viewModel.waveList.observe(this, ::renderList)
    }

    private fun renderList(arrayList: ArrayList<Int>?) {
        if (requireNotNull(arrayList).size != 0) {
            binding.playWave.showWaves(arrayList, getScreenSize().y)
        } else {
            drawShapeBeforeProcessing()
        }
    }

    private fun initialiseView() {
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawShapeBeforeProcessing()

        /*  requireNotNull(intent.extras).getIntArray("data")?.forEach {
              list.add(it)
          }
          binding.playWave.showWaves(list, getScreenSize().y)*/

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

    private fun drawShapeBeforeProcessing() {
        val draw = VerticalLine(this, 4f)
        binding.playWave.addView(View(this).apply {
            minimumHeight = (getScreenSize().y - 116.toPx) / 2
        })
        binding.playWave.addView(draw)
    }


}


