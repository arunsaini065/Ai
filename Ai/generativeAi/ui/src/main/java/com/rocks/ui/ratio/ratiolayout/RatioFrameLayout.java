package com.rocks.ui.ratio.ratiolayout;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class RatioFrameLayout extends FrameLayout implements RatioMeasureDelegate {

    private RatioLayoutDelegate mRatioLayoutDelegate;


    public RatioFrameLayout(Context context) {
        super(context);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attrs);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.update(widthMeasureSpec, heightMeasureSpec);
            widthMeasureSpec = mRatioLayoutDelegate.getWidthMeasureSpec();
            heightMeasureSpec = mRatioLayoutDelegate.getHeightMeasureSpec();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public void setRatio(RatioDatumMode mode, float datumWidth, float datumHeight) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.setRatio(mode, datumWidth, datumHeight);
        }
    }

    @Override
    public void setSquare(boolean square) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.setSquare(square);
        }
    }

    @Override
    public void setAspectRatio(float aspectRatio) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.setAspectRatio(aspectRatio);
        }
    }
    @Override
    public void clearRatio() {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate.setRatio(RatioDatumMode.DATUM_AUTO, 1, 1);
        }
    }
}