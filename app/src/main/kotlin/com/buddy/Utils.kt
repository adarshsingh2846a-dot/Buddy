package com.buddy

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

object Utils {
    fun startPulseAnimation(view: View): ObjectAnimator {
        view.visibility = View.VISIBLE
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f, 1f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0.8f, 0f, 0.8f)
        return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            start()
        }
    }
}
