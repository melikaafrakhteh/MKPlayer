package com.afrakhteh.musicplayer.views.customs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.util.toPx

@SuppressLint("ViewConstructor")
@Suppress("DEPRECATION")
class WaveItem(
        context: Context,
        private val height: Float,
        private val activePercents: Int,
        private val isActive: Boolean
) : View(context) {

    private val baseColor = Paint().apply {
        isAntiAlias = true
        color = resources.getColor(R.color.disableBlue)
    }
    private val activeColor = Paint().apply {
        isAntiAlias = true
        color = resources.getColor(R.color.red)
    }
    private val notActiveColor = Paint().apply {
        isAntiAlias = true
        color = resources.getColor(R.color.shadowNavyBlue)
    }

    init {
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var rect: RectF? = RectF(0f, 0f, width.toPx.toFloat(), height.toPx)
        canvas?.drawRect(rect!!, baseColor)
        rect = null

        var activePercent: RectF? = RectF(0f, 0f, (width * activePercents) / 100f, height.toPx)
        isActiveWave(activePercent, canvas)
        activePercent = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = height.toPx.toInt()
        setMeasuredDimension(measureWidth, measureHeight)

    }

    private fun isActiveWave(activePercent: RectF?, canvas: Canvas?): Boolean {
        if (isActive) {
            // true -> show Color red
            canvas?.drawRect(activePercent!!, activeColor)
            return true

        } else {
            //false -> disable wave
            canvas?.drawRect(activePercent!!, notActiveColor)
            return false
        }
    }
}