package com.rocks.ui.simplecropview.animation

interface SimpleValueAnimatorListener {
    fun onAnimationStarted()
    fun onAnimationUpdated(scale: Float)
    fun onAnimationFinished()
}
