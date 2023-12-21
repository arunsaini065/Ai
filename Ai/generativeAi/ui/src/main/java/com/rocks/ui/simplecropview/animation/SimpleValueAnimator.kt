package com.rocks.ui.simplecropview.animation

@Suppress("unused")
interface SimpleValueAnimator {
    fun startAnimation(duration: Long)
    fun cancelAnimation()
    val isAnimationStarted: Boolean
    fun addAnimatorListener(animatorListener: SimpleValueAnimatorListener?)
}
