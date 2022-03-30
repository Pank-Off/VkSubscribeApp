package ru.punkoff.vksubscribeapp.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CircularTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val background = Paint().apply {
        color = Color.WHITE
        flags = Paint.ANTI_ALIAS_FLAG
    }

    override fun onDraw(canvas: Canvas) {
        val diameter = if (height > width) height else width
        val radius = diameter / 2

        height = diameter
        width = diameter
        canvas.drawCircle(
            (diameter / 2).toFloat(), (diameter / 2).toFloat(),
            radius.toFloat(), background
        )
        super.onDraw(canvas)
    }
}