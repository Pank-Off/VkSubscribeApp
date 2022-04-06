package ru.punkoff.vksubscribeapp.utils

import android.view.View
import android.view.animation.TranslateAnimation
import androidx.core.view.marginBottom

fun setTranslateAnimation(show: Boolean, view: View) {
    view.visibility = View.VISIBLE
    if (show) {
        view.startAnimation(
            TranslateAnimation(
                0f,
                0f,
                (view.height + view.marginBottom).toFloat(),
                0f
            ).apply {
                duration = 100
                fillAfter = true
            }
        )
    } else {
        view.startAnimation(
            TranslateAnimation(
                0f,
                0f,
                0f,
                (view.height + view.marginBottom).toFloat()
            ).apply {
                duration = 100
                fillAfter = true
            }
        )

        view.visibility = View.GONE
    }
}
