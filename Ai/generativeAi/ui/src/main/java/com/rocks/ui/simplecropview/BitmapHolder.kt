package com.rocks.ui.simplecropview

import android.graphics.Bitmap

enum class BitmapHolder {

    INSTANCE;

    private var cropBimap: Bitmap?=null

    companion object {

        fun setBitmap(bitmap: Bitmap?) {

            INSTANCE.cropBimap = bitmap

        }

        fun getBitmap(): Bitmap? {

            return INSTANCE.cropBimap

        }
    }

}