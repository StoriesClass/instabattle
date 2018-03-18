package me.instabattle.app.uiwidgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import java.lang.Math.min

class SquareFrameLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))

        setMeasuredDimension(size, size)
    }
}