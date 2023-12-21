package com.rocks.ui.simplecropview.callback

import android.graphics.Bitmap

interface CropCallback : Callback {
    fun onSuccess(cropped: Bitmap?)
}
