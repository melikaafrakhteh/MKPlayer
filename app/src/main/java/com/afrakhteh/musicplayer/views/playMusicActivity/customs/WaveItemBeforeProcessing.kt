package com.afrakhteh.musicplayer.views.playMusicActivity.customs

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
class WaveItemBeforeProcessing(context: Context,
                               private val width: Float,
                               private val height: Float) : View(context) {

    init {
        invalidate()
    }

    private val baseColor = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.disableBlue)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMeasure = width.toPx.toInt()
        val heightMeasure = height.toPx.toInt()
        setMeasuredDimension(widthMeasure, heightMeasure)

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var recF: RectF? = RectF(0f, 0f, width.toPx, height.toPx)
        canvas?.drawRect(requireNotNull(recF), baseColor)
        recF = null
    }
}