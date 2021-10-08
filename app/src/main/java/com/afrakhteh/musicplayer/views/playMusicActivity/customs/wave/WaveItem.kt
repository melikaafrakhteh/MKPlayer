package com.afrakhteh.musicplayer.views.playMusicActivity.customs.wave

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import androidx.core.content.ContextCompat
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.util.toPx

@SuppressLint("ViewConstructor")
class WaveItem(
        context: Context,
        private val height: Float,
        private val activePercentsRect: Int,
        private val isActive: Boolean
) : View(context) {

    private val baseColor = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.disableBlue)
    }
    private val activeColor = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.red)
    }
    private val notActiveColor = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.shadowNavyBlue)
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

        var activePercentRect: RectF? = RectF(0f, 0f,
                (width * activePercentsRect) / 100f, height.toPx)
        isActiveWave(activePercentRect, canvas)
        activePercentRect = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = height.toPx.toInt()
        setMeasuredDimension(measureWidth, measureHeight)

    }

    private fun isActiveWave(activePercent: RectF?, canvas: Canvas?) {
        // true -> show Color red otherwise disable wave
        if (isActive) {
            canvas?.drawRect(activePercent!!, activeColor)
            return
        }
        canvas?.drawRect(activePercent!!, notActiveColor)
    }
}