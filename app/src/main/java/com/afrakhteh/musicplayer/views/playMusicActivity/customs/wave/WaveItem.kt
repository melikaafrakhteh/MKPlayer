package com.afrakhteh.musicplayer.views.playMusicActivity.customs.wave

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.util.toPx

@SuppressLint("ViewConstructor")
class WaveItem : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

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

    var activePercents: Int = 0
        set(value) {
            field = value
            invalidate()
        }
    var isActive: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    init {
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var rect: RectF? = RectF(0f, 0f, width.toPx.toFloat(), height.toPx.toFloat())
        canvas?.drawRect(rect!!, baseColor)
        rect = null

        var activePercentRect: RectF? = RectF(
                0f, 0f,
                (width * activePercents) / 100f, height.toPx.toFloat()
        )
        isActiveWave(activePercentRect, canvas)
        activePercentRect = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)
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