package com.rocks.ui.cutout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.Log
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.rocks.ui.cutout.BitmapUtility.getResizedBitmap
import com.rocks.ui.R
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

class DrawView(c: Context?, attrs: AttributeSet?) : View(c, attrs)  {
    private var livePath: Path
    private var livePathRepair: Path
    private var pathPaint: Paint
    private var pathPaintRepair: Paint
    var currentBitmap: Bitmap? = null
    private val cuts = Stack<Pair<Pair<Path, Paint>?, Bitmap?>>()
    private val undoneCuts = Stack<Pair<Pair<Path, Paint>?, Bitmap?>>()
    private var pathX = 0f
    private var pathY = 0f
    private var undoButton: AppCompatImageView? = null
    private var redoButton: AppCompatImageView? = null
    private var loadingModal: View? = null
    private var currentAction: DrawViewAction? = null
    private var cursorPoint: Point? = null
    private var cursorDistance = 250
    private var strokeWidth: Float = 0f
    private var strokeWidthLast: Float = 0f
    private var isRepair = false
    private var mBitmapShader: BitmapShader? = null
    var flgPathDraw = true
    var misfires: Point? = null
    var bFirstPoint: Boolean = true
    var mLastPoint: Point? = null
    private var points: ArrayList<Point?>
    private var pointsDraw: ArrayList<Point?>? = null
    private var paintCutout: Paint? = null
    var onTouchEventListener: OnTouchEventListener? = null
    private var ballIsClear = true
    private val _redoUndo by lazy { RedoUndo() }
    var touchCheck:Boolean=false
    enum class DrawViewAction {
        AUTO_CLEAR, MANUAL_CLEAR, ZOOM, CUTOUT, REPAIR,NONE
    }


    fun clear() {
        ballIsClear = true
    }

    fun clearWithInvalidate() {
        ballIsClear = true
         invalidate()
    }

    fun activeBallView() {
        if (ballIsClear) {
            ballIsClear = false
            invalidate()
        }
    }


    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 8f
        }
    }

    private val paintInternal by lazy {
        Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.red_trans)
        }
    }

    private val paintFinger by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
        }
    }


    fun setRepair(repair: Boolean) {
        isRepair = repair
    }

    fun setButtons(undoButton: AppCompatImageView?, redoButton: AppCompatImageView?) {
        this.undoButton = undoButton
        this.redoButton = redoButton
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)

        resizeBitmap(newWidth, newHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()

        if (currentBitmap != null) {
            canvas.drawBitmap(currentBitmap!!, 0f, 0f, null)
            for (action in cuts) {
                if (action.first != null) {
                    canvas.drawPath(action.first?.first!!, action.first?.second!!)
                }
            }
            if (currentAction == DrawViewAction.MANUAL_CLEAR) {
                if (isRepair) {
                    pathPaintRepair.shader = mBitmapShader
                    canvas.drawPath(livePathRepair, pathPaintRepair)
                } else {
                    canvas.drawPath(livePath, pathPaint)
                }
            }
        }
        if (currentAction == DrawViewAction.CUTOUT) {
            val path = Path()
            var first = true
            var i = 0
            while (i < points.size) {
                val point = points[i]
                when {
                    first -> {
                        first = false
                        path.moveTo(point!!.x.toFloat(), point.y.toFloat())
                    }
                    i < points.size - 1 -> {
                        val next = points[i + 1]
                        path.quadTo(
                            point?.x?.toFloat()!!,
                            point.y.toFloat(),
                            next?.x?.toFloat()!!,
                            next.y.toFloat()
                        )
                    }
                    else -> {
                        mLastPoint = points[i]
                        path.lineTo(point!!.x.toFloat(), point.y.toFloat())
                    }
                }
                i += 2
            }
            paintCutout?.let { canvas.drawPath(path, it) }
        }
        if (cursorPoint != null && currentAction != DrawViewAction.ZOOM && ballIsClear.not()) {

            canvas.drawCircle(
                cursorPoint?.x?.toFloat()!!,
                cursorPoint?.y?.toFloat()!!,
                strokeWidth,
                paint
            )

            canvas.drawCircle(
                cursorPoint?.x?.toFloat()!!,
                cursorPoint?.y?.toFloat()!!,
                strokeWidth,
                paintInternal
            )

            canvas.drawCircle(
                cursorPoint?.x?.toFloat()!!,
                cursorPoint?.y?.toFloat()!! + cursorDistance,
                24f,
                paintFinger
            )

        }
        canvas.restore()

    }

    private fun touchStart(x: Float, y: Float) {
        onTouchEventListener?.onTouchStart()
        Log.d("dkjshd","fd"+currentAction)
         touchCheck=true
        pathX = x
        pathY = y
        undoneCuts.clear()
        redoButton?.isEnabled = false


        if (currentAction == DrawViewAction.AUTO_CLEAR) {
            AutomaticPixelClearingTask(this).execute(x.toInt(), y.toInt())
        } else {
            if (isRepair) {
                livePathRepair.moveTo(x, y)
            } else {
                livePath.moveTo(x, y)
            }
        }

        invalidate()
    }

    private fun touchMove(x: Float, y: Float) {
        if (currentAction == DrawViewAction.MANUAL_CLEAR) {
            val dx = abs(x - pathX)
            val dy = abs(y - pathY)
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                if (isRepair) {
                    livePathRepair.quadTo(pathX, pathY, (x + pathX) / 2, (y + pathY) / 2)
                } else {
                    livePath.quadTo(pathX, pathY, (x + pathX) / 2, (y + pathY) / 2)
                }
                pathX = x
                pathY = y
            }
        }

    }

    private fun touchUp() {
        if (currentAction == DrawViewAction.MANUAL_CLEAR) {
            if (isRepair) {
                livePathRepair.lineTo(pathX, pathY)
                cuts.push(Pair(Pair(livePathRepair, pathPaintRepair), null))
                livePathRepair = Path()
            } else {
                livePath.lineTo(pathX, pathY)
                cuts.push(Pair(Pair(livePath, pathPaint), null))
                livePath = Path()
            }
            undoButton?.isEnabled = true
             clearWithInvalidate()
             currentBitmap = getViewBitmap()
            _redoUndo.doAction(DrawViewState(currentAction,currentBitmap, null,null))
            cuts.clear()
            undoneCuts.clear()
            activeBallView()
        }
        onTouchEventListener?.onTouchStop()

    }

    fun undo() {

       if (cuts.size > 0) {

            val cut = cuts.pop()

            if (cut.second != null) {


                undoneCuts.push(Pair(null, currentBitmap))

                currentBitmap = cut.second

            } else {

                undoneCuts.push(cut)

            }

            if (cuts.isEmpty()) {

                undoButton?.isEnabled = false
            }

            redoButton?.isEnabled = true

            invalidate()

        }else{

           _redoUndo.undoAction()

       }

    }

    fun redo() {

        if (undoneCuts.size > 0) {
            val cut = undoneCuts.pop()
            if (cut.second != null) {
                cuts.push(Pair(null, currentBitmap))
                currentBitmap = cut.second
            } else {
                cuts.push(cut)


            }
            if (undoneCuts.isEmpty()) {
                redoButton?.isEnabled = false
            }
            undoButton?.isEnabled = true
            invalidate()
        }else{
            _redoUndo.redoAction()
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        Log.d("asdf","sdfsd"+currentAction)

        if (currentBitmap != null && currentAction != DrawViewAction.ZOOM && currentAction != DrawViewAction.CUTOUT) {
            cursorPoint = Point(ev.x.roundToInt(), ev.y.roundToInt() - cursorDistance)
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchStart(ev.x, ev.y - cursorDistance)
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    touchMove(ev.x, ev.y - cursorDistance)
                    invalidate()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    touchUp()
                    invalidate()
                    return true
                }
            }
            return super.onTouchEvent(ev)
        } else if (currentAction == DrawViewAction.CUTOUT) {
            val point = Point()
            point.x = ev.x.toInt()
            point.y = ev.y.toInt()
            if (flgPathDraw) {
                if (bFirstPoint) {
                    if (comparePoint(misfires, point)) {
                        points.add(misfires)
                        flgPathDraw = false
                        pointsDraw = points
                        resetView()
                    } else {
                        points.add(point)
                    }
                } else {
                    points.add(point)
                }
                if (!bFirstPoint) {
                    misfires = point
                    bFirstPoint = true
                }
            }


            invalidate()
            if (ev.action == MotionEvent.ACTION_DOWN) {
                onTouchEventListener?.onTouchStart()
            }
            if (ev.action == MotionEvent.ACTION_UP) {
                mLastPoint = point
                if (flgPathDraw) {
                    if (points.size > 12) {
                        if (!comparePoint(misfires, mLastPoint)) {
                            flgPathDraw = false
                            points.add(misfires)
                            cropArea()
                        }
                    }
                }
            }
            return true
        } else if (currentAction == DrawViewAction.ZOOM) {

            onTouchEventListener?.onTouchZoom()

            return true
        } else {
            return super.onTouchEvent(ev)
        }

    }

    private fun resizeBitmap(width: Int, height: Int) {
        if (width > 0 && height > 0 && currentBitmap != null) {

            if (cursorPoint==null) {

                cursorPoint = Point(width / 2, height / 2 - cursorDistance)

            }
            currentBitmap = getResizedBitmap(currentBitmap!!, width, height)
            currentBitmap?.setHasAlpha(true)

            invalidate()

        }

    }

    fun setBitmapAi(bitmap: Bitmap?) {

        if(bitmap!=null) {

            currentBitmap = bitmap

            resizeBitmap(width, height)

            _redoUndo.doAction(DrawViewState(currentAction,currentBitmap, null,null))

            cuts.clear()

            undoneCuts.clear()

        }

    }


    fun setBitmap(bitmap: Bitmap?) {

        if(bitmap!=null) {


            currentBitmap = bitmap

            resizeBitmap(width, height)

            _redoUndo.setOriginalBimap(bitmap.cloneBitmap())

            _redoUndo.doAction(DrawViewState(currentAction,currentBitmap, null,null))

            cuts.clear()

            undoneCuts.clear()
             if (mBitmapShader==null) {
                 mBitmapShader = BitmapShader(currentBitmap!!, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)
             }

        }

    }

    fun setAction(newAction: DrawViewAction?) {

        currentAction = newAction

        if (currentAction != DrawViewAction.CUTOUT)
        {
            resetView()
        } else {
            invalidate()
        }
        setRepair(currentAction == DrawViewAction.REPAIR)
        if (currentAction == DrawViewAction.REPAIR) {
            currentAction = DrawViewAction.MANUAL_CLEAR
        }

    }
    fun setStrokeWidth(strokeWidth: Int) {
        pathPaint = Paint(pathPaint)
        pathPaintRepair = Paint(pathPaintRepair)
        pathPaint.strokeWidth = 2 * strokeWidth.toFloat()
        pathPaintRepair.strokeWidth = 2 * strokeWidth.toFloat()
        this.strokeWidth = strokeWidth.toFloat()
        this.strokeWidthLast = strokeWidth.toFloat()
        invalidate()
    }

    fun setStrokeWidth(scale: Float) {
        pathPaint = Paint(pathPaint)
        pathPaintRepair = Paint(pathPaintRepair)
        strokeWidth = strokeWidthLast / scale
        pathPaint.strokeWidth = 2 * strokeWidth
        pathPaintRepair.strokeWidth = 2 * strokeWidth
        invalidate()
    }

    fun setOffset(offset: Int) {
        cursorDistance = offset
        invalidate()
    }


    private fun comparePoint(first: Point?, current: Point?): Boolean {
        return if ((current!!.x - 3) < first!!.x && first.x < (current!!.x + 3)
            && (current!!.y - 3) < first.y && first.y < (current!!.y + 3)
        ) {
            points.size >= 10
        } else {
            false
        }
    }


    @SuppressLint("StaticFieldLeak")
    private inner class AutomaticPixelClearingTask constructor(drawView: DrawView) :
        AsyncTask<Int?, Void?, Bitmap?>() {
        private val drawViewWeakReference: WeakReference<DrawView> = WeakReference(drawView)
        override fun onPreExecute() {
            super.onPreExecute()
            drawViewWeakReference.get()?.loadingModal?.visibility = VISIBLE
            drawViewWeakReference.get()?.cuts?.push(
                Pair(
                    null,
                    drawViewWeakReference.get()?.currentBitmap
                )
            )
        }

        override fun doInBackground(vararg points: Int?): Bitmap? {
            runCatching {

                val oldBitmap = drawViewWeakReference.get()?.currentBitmap
                val colorToReplace = oldBitmap?.getPixel(points[0]!!, points[1]!!)
                val width = oldBitmap?.width
                val height = oldBitmap?.height
                val pixels = IntArray(width!! * height!!)
                oldBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                val rA = Color.alpha(colorToReplace!!)
                val rR = Color.red(colorToReplace)
                val rG = Color.green(colorToReplace)
                val rB = Color.blue(colorToReplace)
                var pixel: Int

                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val index = y * width + x
                        pixel = pixels[index]
                        val rrA = Color.alpha(pixel)
                        val rrR = Color.red(pixel)
                        val rrG = Color.green(pixel)
                        val rrB = Color.blue(pixel)
                        if (rA - COLOR_TOLERANCE < rrA && rrA < rA + COLOR_TOLERANCE && rR - COLOR_TOLERANCE < rrR && rrR < rR + COLOR_TOLERANCE && rG - COLOR_TOLERANCE < rrG && rrG < rG + COLOR_TOLERANCE && rB - COLOR_TOLERANCE < rrB && rrB < rB + COLOR_TOLERANCE) {
                            pixels[index] = Color.TRANSPARENT
                        }
                    }
                }
                val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
                return newBitmap
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result!=null) {
                super.onPostExecute(result)
                drawViewWeakReference.get()?.currentBitmap = result
                drawViewWeakReference.get()?.undoButton?.isEnabled = true
                drawViewWeakReference.get()?.loadingModal?.visibility = INVISIBLE
                _redoUndo.doAction(DrawViewState(currentAction,result, null,null))
                drawViewWeakReference.get()?.cuts?.clear()
                drawViewWeakReference.get()?.undoneCuts?.clear()
                drawViewWeakReference.get()?.invalidate()
            }
        }

    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
        private const val COLOR_TOLERANCE = 20f
    }

    init {
        livePath = Path()
        livePathRepair = Path()
        pathPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pathPaint.isDither = true
        pathPaint.color = Color.TRANSPARENT
        pathPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeJoin = Paint.Join.ROUND
        pathPaint.strokeCap = Paint.Cap.ROUND
        pathPaintRepair = Paint(Paint.ANTI_ALIAS_FLAG)
        pathPaintRepair.isDither = true
        pathPaintRepair.color = Color.BLACK
        pathPaintRepair.style = Paint.Style.STROKE
        pathPaintRepair.strokeJoin = Paint.Join.ROUND
        pathPaintRepair.strokeCap = Paint.Cap.ROUND
        paintCutout = Paint(Paint.ANTI_ALIAS_FLAG)
        paintCutout?.style = Paint.Style.STROKE
        paintCutout?.pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
        paintCutout?.strokeWidth = 5f
        paintCutout?.color = Color.RED
        paintCutout?.strokeJoin = Paint.Join.ROUND
        paintCutout?.strokeCap = Paint.Cap.ROUND
        points = ArrayList()
        bFirstPoint = false

        _redoUndo.redoUndoCallback = object :() -> Unit {

            override fun invoke() {

                val action = _redoUndo.getAction()

                currentBitmap = if (action?.bitmap!=null && action.stateName!=null) {

                    action.bitmap

                }else{

                    _redoUndo.getOriginalBimap()

                }
                if (currentBitmap?.width!=width || currentBitmap?.height!=height) {

                    resizeBitmap(width, height)

                }

                invalidate()

            }

        }
    }

    private fun cropArea() {
        pointsDraw = points
        resetView()
        currentBitmap = getViewBitmap()
        currentBitmap = cropImage(currentBitmap)
        onTouchEventListener?.onTouchStop()
        invalidate()
    }

    fun resetView() {
        points = ArrayList()
        bFirstPoint = false
        flgPathDraw = true
        invalidate()
    }

    private fun cropImage(image: Bitmap?): Bitmap {
        val cropImage =
            Bitmap.createBitmap(currentBitmap!!.width, currentBitmap!!.height, image!!.config)
        val canvas = Canvas(cropImage)
        val paint = Paint()
        paint.isAntiAlias = true
        val path = Path()
        for (i in pointsDraw?.indices!!) {
            path.lineTo(pointsDraw?.get(i)!!.x.toFloat(), pointsDraw!![i]!!.y.toFloat())
        }
        canvas.drawPath(path, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(currentBitmap!!, 0f, 0f, paint)
        _redoUndo.doAction(DrawViewState(currentAction,cropImage, null,null))
        cuts.clear()
        undoneCuts.clear()
        return cropImage
    }

    interface OnTouchEventListener {

        fun onTouchStart()
        fun onTouchStop()
        fun onTouchZoom()

    }

}