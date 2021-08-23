package com.afrakhteh.musicplayer.views.customs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.afrakhteh.musicplayer.util.toPx
import kotlin.random.Random

class WaveView : LinearLayout {

    private var percents: ArrayList<Int> = ArrayList()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        orientation = VERTICAL
    }

    fun showWaves(percents: ArrayList<Int>) {
        this.percents.apply {
            clear()
            addAll(percents)
        }

        repeat(percents.size) { index ->
            val randomActiveNumber = Random.nextInt(0, 50)

            if (index <= 40) {
                addView(WaveItem(context, 4f, randomActiveNumber, true))
                addView(View(context).apply {
                    minimumHeight = 4.toPx
                })
            } else {
                addView(WaveItem(context, 4f, randomActiveNumber, false))
                addView(View(context).apply {
                    minimumHeight = 4.toPx
                })
            }
        }
    }
}
