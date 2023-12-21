package com.rocks.ui.cutout

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import com.rocks.ui.cutout.BitmapUtility.getBorderedBitmap

class CutOut(val drawView: DrawView?){

    private var source: Bitmap? = null
    private var bordered = false
    private var borderColor = Color.WHITE
    private var activityBuilder: ActivityBuilder?=null
    fun active(): ActivityBuilder?{
        return if (activityBuilder!=null){
            activityBuilder
        }else {
            activityBuilder = ActivityBuilder()
            activityBuilder
        }
    }


  inner  class ActivityBuilder {
        fun init(): ActivityBuilder {
            drawView?.isDrawingCacheEnabled = true
            drawView?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            drawView?.setStrokeWidth(40)
            drawView?.activeBallView()
            drawView?.setAction(DrawView.DrawViewAction.MANUAL_CLEAR)
            return this
        }

      fun src(source: Bitmap?): ActivityBuilder {
            this@CutOut.source = source
            drawView?.setBitmap(source)
            return this
        }

        fun bordered(): ActivityBuilder {
            bordered = true
            return this
        }

        fun bordered(borderColor: Int): ActivityBuilder {
            this@CutOut.borderColor = borderColor
            return bordered()
        }

      fun getCutOut(): Bitmap? {
          drawView?.clear()
          drawView?.resetView()
          return if (borderColor!=-1) {
              drawView?.let { it.getViewBitmap()?.let { it1 -> getBorderedBitmap(it1, borderColor, BORDER_SIZE) } }
          } else {
              drawView?.getViewBitmap()
          }
      }

      fun setAction(value: DrawView.DrawViewAction) {

          drawView?.setAction(value)
      }

      fun setUndo(){
          drawView?.undo()

      }
      fun setRedo()
      {
          drawView?.redo()
      }

      fun setBallSize(value: Int){

          drawView?.setStrokeWidth(value)

      }

      fun showBall() {
          drawView?.activeBallView()
      }

      fun hideBall(){
          drawView?.clearWithInvalidate()
      }

  }
    companion object {
        private const val BORDER_SIZE: Int = 45
    }
}