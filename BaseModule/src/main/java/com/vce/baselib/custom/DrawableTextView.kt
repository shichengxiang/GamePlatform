package com.vce.baselib.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.graphics.drawable.Drawable


/**
 * 描述：DrawableTextView
 * 创建者: shichengxiang
 * 创建时间：2022/2/25
 */
class DrawableTextView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    override fun onDraw(canvas: Canvas?) {
        val compoundDrawables: Array<Drawable>? = compoundDrawables
        if (compoundDrawables != null) {
            val left = compoundDrawables[0]
            val top = compoundDrawables[1]
            val right = compoundDrawables[2]
            val bottom = compoundDrawables[3]
            var paddingHorizontal = 0
            var paddingVertical = 0
            var spaceHorizontal = width
            var spaceVertical = height
            if (left != null) {
                spaceHorizontal = spaceHorizontal - compoundDrawablePadding - left.intrinsicWidth
            }
            if (right != null) {
                spaceHorizontal = spaceHorizontal - compoundDrawablePadding - right.intrinsicWidth
            }
            spaceHorizontal -= paint.measureText(text.toString())
                .toInt()
            paddingHorizontal = spaceHorizontal / 2
            if (top != null) {
                spaceVertical -= compoundDrawablePadding + top.intrinsicHeight
            }
            if (bottom != null) {
                spaceVertical -= compoundDrawablePadding + bottom.intrinsicHeight
            }
            val fontMetrics = paint.fontMetrics
            val txtHeight = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading
            spaceVertical -= txtHeight.toInt()
            paddingVertical = spaceVertical / 2
            setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
//            canvas?.translate(deltaX,deltaY)
        }

        super.onDraw(canvas)
    }
}