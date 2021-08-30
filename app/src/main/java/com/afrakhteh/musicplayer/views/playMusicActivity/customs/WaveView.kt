package com.afrakhteh.musicplayer.views.playMusicActivity.customs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.afrakhteh.musicplayer.model.dataSource.AudioWaveDataSource
import com.afrakhteh.musicplayer.util.toPx


class WaveView : LinearLayout {

    private var percents: ArrayList<Int> = ArrayList()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        orientation = VERTICAL
    }

    fun showWaves(percents: ArrayList<Int>, screenHeight: Int) {
        this.percents.apply {
            clear()
            addAll(percents)
        }
        val halfHeight = (screenHeight - 116.toPx) / 2

        addView(View(context).apply {
            minimumHeight = halfHeight
        })
        repeat(percents.size) { index ->
            //  val randomActiveNumber = Random.nextInt(0, 50)

            if (index <= 40) {
                addView(WaveItem(context, 4f, setActiveAudioPercents(), true))
                addView(View(context).apply {
                    minimumHeight = 4.toPx
                })
            } else {
                addView(WaveItem(context, 4f, setActiveAudioPercents(), false))
                addView(View(context).apply {
                    minimumHeight = 4.toPx
                })
            }
        }
        addView(View(context).apply {
            minimumHeight = halfHeight
        })
    }

    private fun setActiveAudioPercents(): Int {

        var activePercent = 0
        repeat(AudioWaveDataSource().calculateSamplesPerFrame()) {
            activePercent = it
        }

        return activePercent
    }
}
