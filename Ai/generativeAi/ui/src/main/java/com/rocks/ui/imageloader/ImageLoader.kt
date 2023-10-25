package com.rocks.ui.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View


class ImageLoader(private val mUri: Uri?,val view: View,val callbacks:(Bitmap?)->Unit): AsyncTaskCoroutine<Bitmap>() {

    private var mWidth=0
    private var mHeight=0


    val context: Context get() = view.context


          init {

               val metrics=view.resources.displayMetrics
               val densityAdj = if (metrics.density > 1) (1 / metrics.density).toDouble() else 1.toDouble()
               mWidth = (metrics.widthPixels * densityAdj).toInt()
               mHeight = (metrics.heightPixels * densityAdj).toInt()


          }

    override fun doInBackground(): Bitmap? {
        runCatching {

            val decodeResult: BitmapUtils.BitmapSampled =
                BitmapUtils.decodeSampledBitmap(context, mUri, mWidth, mHeight)

            val rotateResult: BitmapUtils.RotateBitmapResult =
                BitmapUtils.rotateBitmapByExif(decodeResult.bitmap, context, mUri, true)

            return rotateResult.bitmap
        }
        return null
    }

    override fun onPreExecute() {

    }
    fun setRatio(h:Int,w:Int): ImageLoader {
        mHeight=h
        mWidth=w
        return this
    }

    override fun onPostExecute(result: Bitmap?) {


        Handler(Looper.getMainLooper()).post {

             callbacks(result)

        }


    }
}