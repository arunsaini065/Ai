package com.rocks.ui.ratio.ratiolayout

import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.rocks.ui.R


/**
 * author: Amphiaraus
 * since : 2017/9/13 上午10:39.
 */
class RatioLayoutDelegate<TARGET> private constructor(
    private val mRatioTarget: TARGET,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int,
) where TARGET : View?, TARGET : RatioMeasureDelegate? {
    private var mRatioDatumMode: RatioDatumMode?
    private var mDatumWidth: Float=0f
    private var mDatumHeight: Float=0f
    private var mAspectRatio: Float=0f
    private var mIsSquare: Boolean
    var widthMeasureSpec = 0
        private set
    var heightMeasureSpec = 0
        private set

    private fun shouldRatioDatumMode(params: ViewGroup.LayoutParams): RatioDatumMode? {
        if (mRatioDatumMode == null || mRatioDatumMode === RatioDatumMode.DATUM_AUTO) {
            if (params.width > 0 || shouldLinearParamsWidth(params)
                || params.width == ViewGroup.LayoutParams.MATCH_PARENT
            ) {
                return RatioDatumMode.DATUM_WIDTH
            }
            return if (params.height > 0 || shouldLinearParamsHeight(params)
                || params.height == ViewGroup.LayoutParams.MATCH_PARENT
            ) {
                RatioDatumMode.DATUM_HEIGHT
            } else null
        }
        return mRatioDatumMode
    }

    private fun shouldLinearParamsWidth(params: ViewGroup.LayoutParams): Boolean {
        if (params !is LinearLayout.LayoutParams) {
            return false
        }
        val layoutParams = params
        return layoutParams.width == 0 && layoutParams.weight > 0
    }

    private fun shouldLinearParamsHeight(params: ViewGroup.LayoutParams): Boolean {
        if (params !is LinearLayout.LayoutParams) {
            return false
        }
        val layoutParams = params
        return layoutParams.height == 0 && layoutParams.weight > 0
    }

    fun update(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.widthMeasureSpec = widthMeasureSpec
        this.heightMeasureSpec = heightMeasureSpec
        val mode = shouldRatioDatumMode(mRatioTarget!!.layoutParams)
        val wp = mRatioTarget.paddingLeft + mRatioTarget.paddingRight
        val hp = mRatioTarget.paddingTop + mRatioTarget.paddingBottom
        if (mode === RatioDatumMode.DATUM_WIDTH) {
            val width = View.MeasureSpec.getSize(widthMeasureSpec)
            if (mIsSquare) {
                val height = resolveSize(width - wp + hp, heightMeasureSpec)
                this.heightMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            } else if (mAspectRatio > 0) {
                val height =
                    resolveSize(Math.round((width - wp) / mAspectRatio + hp), heightMeasureSpec)
                this.heightMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            } else if (mDatumWidth > 0 && mDatumHeight > 0) {
                val height = resolveSize(Math.round((width - wp) / mDatumWidth * mDatumHeight + hp),
                    heightMeasureSpec)
                this.heightMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            }
        } else if (mode === RatioDatumMode.DATUM_HEIGHT) {
            val height = View.MeasureSpec.getSize(heightMeasureSpec)
            if (mIsSquare) {
                val width = resolveSize(height - hp + wp, widthMeasureSpec)
                this.widthMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            } else if (mAspectRatio > 0) {
                val width =
                    resolveSize(Math.round((height - hp) / mAspectRatio + wp), widthMeasureSpec)
                this.widthMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            } else if (mDatumWidth > 0 && mDatumHeight > 0) {
                val width = resolveSize(Math.round((height - hp) / mDatumHeight * mDatumWidth + wp),
                    widthMeasureSpec)
                this.widthMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            }
        }
    }

    private fun requestLayout() {
        mRatioTarget!!.requestLayout()
    }

    private fun resolveSize(size: Int, measureSpec: Int): Int {
        /*return View.resolveSize(size,measureSpec);*/
        return size
    }

    fun setRatio(mode: RatioDatumMode?, datumWidth: Float, datumHeight: Float) {
        mRatioDatumMode = mode
        mDatumWidth = datumWidth
        mDatumHeight = datumHeight
        requestLayout()
    }

    fun setSquare(square: Boolean) {
        mIsSquare = square
        requestLayout()
    }

    fun setAspectRatio(aspectRatio: Float) {
        mAspectRatio = aspectRatio
        requestLayout()
    }

    companion object {
        @JvmStatic
        fun <TARGET> obtain(
            target: TARGET,
            attrs: AttributeSet?,
        ): RatioLayoutDelegate<*> where TARGET : View?, TARGET : RatioMeasureDelegate? {
            return obtain(target, attrs, 0)
        }
        @JvmStatic
        fun <TARGET> obtain(
            target: TARGET,
            attrs: AttributeSet?,
            defStyleAttr: Int,
        ): RatioLayoutDelegate<*> where TARGET : View?, TARGET : RatioMeasureDelegate? {
            return obtain(target, attrs, 0, 0)
        }
        @JvmStatic
        fun <TARGET> obtain(
            target: TARGET,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int,
        ): RatioLayoutDelegate<*> where TARGET : View?, TARGET : RatioMeasureDelegate? {
            return RatioLayoutDelegate(target, attrs, defStyleAttr, defStyleRes)
        }
    }

    init {
        val a = mRatioTarget?.context?.obtainStyledAttributes(attrs,
            R.styleable.ViewSizeCalculate,
            defStyleAttr,
            defStyleRes)!!
        mRatioDatumMode =
            RatioDatumMode.valueOf(a.getInt(R.styleable.ViewSizeCalculate_datumRatio, 0))
        mDatumWidth = a.getFloat(R.styleable.ViewSizeCalculate_widthRatio, mDatumWidth)
        mDatumHeight = a.getFloat(R.styleable.ViewSizeCalculate_heightRatio, mDatumHeight)
        mIsSquare = a.getBoolean(R.styleable.ViewSizeCalculate_layoutSquare, false)
        mAspectRatio = a.getFloat(R.styleable.ViewSizeCalculate_layoutAspectRatio, mAspectRatio)
        a.recycle()
    }
}