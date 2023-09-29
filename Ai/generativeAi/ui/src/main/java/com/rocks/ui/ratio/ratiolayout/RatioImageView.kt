package com.rocks.ui.ratio.ratiolayout

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.rocks.ui.ratio.ratiolayout.RatioLayoutDelegate.Companion.obtain

/**
 * author: Amphiaraus
 * since : 2017/9/13 上午10:39.
 */
class RatioImageView : AppCompatImageView, RatioMeasureDelegate {
    private var mRatioLayoutDelegate: RatioLayoutDelegate<*>? = null

    constructor(context: Context) : super(context) {
        mRatioLayoutDelegate = obtain(this, null)

    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mRatioLayoutDelegate = obtain(this, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        mRatioLayoutDelegate = obtain(this, attrs, defStyleAttr)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate?.update(widthMeasureSpec, heightMeasureSpec)
            widthMeasureSpec = mRatioLayoutDelegate!!.widthMeasureSpec
            heightMeasureSpec = mRatioLayoutDelegate!!.heightMeasureSpec
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun setSquare(square: Boolean) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate?.setSquare(square)
        }
    }

    override fun setAspectRatio(aspectRatio: Float) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate?.setAspectRatio(aspectRatio)
        }
    }

    override fun setRatio(mode: RatioDatumMode?, datumWidth: Float, datumHeight: Float) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate?.setRatio(mode, datumWidth, datumHeight)
        }
    }
    override fun clearRatio() {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate!!.setRatio(RatioDatumMode.DATUM_AUTO, 1f, 1f)
        }
    }
}