package com.rocks.ui.cutout

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.View


fun View.getViewBitmap(): Bitmap? {
    clearFocus()
    isPressed = false
    val willNotCache: Boolean = willNotCacheDrawing()
    setWillNotCacheDrawing(false)

    val color: Int = drawingCacheBackgroundColor
    drawingCacheBackgroundColor = 0
    if (color != 0) {
        destroyDrawingCache()
    }


    buildDrawingCache()
    val cacheBitmap: Bitmap = drawingCache ?: return null
    val bitmap = Bitmap.createBitmap(cacheBitmap)

    destroyDrawingCache()
    setWillNotCacheDrawing(willNotCache)
    drawingCacheBackgroundColor = color
    return bitmap
}


fun handler( timeOut: Long = 0,callback:()->Unit){

    Handler(Looper.getMainLooper()).postDelayed({

        callback()


    },timeOut)


}