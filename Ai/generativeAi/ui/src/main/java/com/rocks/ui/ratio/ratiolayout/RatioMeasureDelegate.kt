package com.rocks.ui.ratio.ratiolayout

import com.rocks.ui.ratio.ratiolayout.RatioDatumMode

interface RatioMeasureDelegate {
    fun setRatio(mode: RatioDatumMode?, datumWidth: Float, datumHeight: Float)
    fun setSquare(square: Boolean)
    fun setAspectRatio(aspectRatio: Float)
    fun clearRatio()
}