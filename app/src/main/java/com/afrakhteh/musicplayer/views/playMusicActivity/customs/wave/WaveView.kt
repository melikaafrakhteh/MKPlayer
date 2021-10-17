package com.afrakhteh.musicplayer.views.playMusicActivity.customs.wave

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.afrakhteh.musicplayer.util.toPx

class WaveView : LinearLayout {

    private var percents: ArrayList<Int> = ArrayList()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        orientation = VERTICAL
    }

    fun showWaves(percents: List<Int>, screenHeight: Int) {
        this.percents.apply {
            if (size == percents.size) {
                clear()
                addAll(percents)
            }
        }

        val halfHeight = (screenHeight - 116.toPx) / 2

        addView(View(context).apply {
            minimumHeight = halfHeight
        })
        repeat(percents.size) { index ->
            addView(WaveItem(context, 4f, percents[index], true))
            addView(View(context).apply {
                minimumHeight = 4.toPx
            })
        }
        addView(View(context).apply {
            minimumHeight = halfHeight
        })
    }
}
