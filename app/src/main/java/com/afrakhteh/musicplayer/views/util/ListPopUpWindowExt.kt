package com.afrakhteh.musicplayer.views.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListAdapter
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.util.toPx
import kotlin.math.max


fun ListAdapter.measureContentWidth(context: Context): Int {
    var itemType = 0
    var itemView: View? = null
    var mMeasureParent: ViewGroup? = null
    var maxWidth = 0

    val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            0, View.MeasureSpec.UNSPECIFIED
    )
    val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            0, View.MeasureSpec.UNSPECIFIED
    )

    val count: Int = count
    for (i in 0 until count) {
        val positionType: Int = getItemViewType(i)
        if (positionType != itemType) {
            itemType = positionType
            itemView = null
        }
        if (mMeasureParent == null) {
            mMeasureParent = FrameLayout(context)
        }
        itemView = getView(i, itemView, mMeasureParent)
        itemView.measure(widthMeasureSpec, heightMeasureSpec)

        val itemWidth: Int = itemView.measuredWidth
        if (itemWidth > maxWidth) {
            maxWidth = itemWidth
        }
    }
    return max(maxWidth, Numerals.MAX_POPUP_DIALOG.toPx)
}
