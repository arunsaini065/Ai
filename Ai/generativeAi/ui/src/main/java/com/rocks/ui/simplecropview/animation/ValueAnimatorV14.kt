package com.rocks.ui.simplecropview.animation

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.TargetApi
import android.os.Build
import android.view.animation.Interpolator

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class ValueAnimatorV14(interpolator: Interpolator?) : SimpleValueAnimator,
    Animator.AnimatorListener, AnimatorUpdateListener {
    private val animator: ValueAnimator
    private var animatorListener: SimpleValueAnimatorListener =
        object : SimpleValueAnimatorListener {
            override fun onAnimationStarted() {}
            override fun onAnimationUpdated(scale: Float) {}
            override fun onAnimationFinished() {}
        }

    init {
        animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.addListener(this)
        animator.addUpdateListener(this)
        animator.interpolator = interpolator
    }

    override fun startAnimation(duration: Long) {
        if (duration >= 0) {
            animator.setDuration(duration)
        } else {
            animator.setDuration(DEFAULT_ANIMATION_DURATION.toLong())
        }
        animator.start()
    }

    override fun cancelAnimation() {
        animator.cancel()
    }

    override val isAnimationStarted: Boolean
        get() = animator.isStarted

    override fun addAnimatorListener(animatorListener: SimpleValueAnimatorListener?) {
        if (animatorListener != null) this.animatorListener = animatorListener
    }

    override fun onAnimationStart(animation: Animator) {
        animatorListener.onAnimationStarted()
    }

    override fun onAnimationEnd(animation: Animator) {
        animatorListener.onAnimationFinished()
    }

    override fun onAnimationCancel(animation: Animator) {
        animatorListener.onAnimationFinished()
    }

    override fun onAnimationRepeat(animation: Animator) {}
    override fun onAnimationUpdate(animation: ValueAnimator) {
        animatorListener.onAnimationUpdated(animation.animatedFraction)
    }

    companion object {
        private const val DEFAULT_ANIMATION_DURATION = 150
    }
}
