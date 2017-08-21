package com.death.movieticket

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.LinearLayout


/**
 * Created by sidhantrajora on 21/08/17.
 */
class TicketView : LinearLayout {
    lateinit var bm: Bitmap
    lateinit private var cv: Canvas
    lateinit private var eraser: Paint
    private val holesBottomMargin = 70f
    private val holeRadius = 40f

    constructor(context: Context) : super(context) {
        Init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        Init()
    }

    constructor(context: Context, attrs: AttributeSet,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        Init()
    }

    private fun Init() {
        eraser = Paint()
        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        eraser.isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            cv = Canvas(bm)
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        bm.eraseColor(Color.TRANSPARENT)

        cv.drawColor(Color.WHITE)

        val paint = Paint()
        paint.setARGB(255, 250, 250, 250)
        paint.strokeWidth = 0f
        paint.style = Paint.Style.FILL
        cv.drawRect(0f, h, w, h - pxFromDp(context, holesBottomMargin), paint)


        cv.drawCircle(0f, 0f, holeRadius, eraser)
        cv.drawCircle(w / 2, 0f, holeRadius, eraser)
        cv.drawCircle(w, 0f, holeRadius, eraser)
        cv.drawCircle(0f, h - pxFromDp(context, holesBottomMargin), holeRadius, eraser)
        cv.drawCircle(w, h - pxFromDp(context, holesBottomMargin), holeRadius, eraser)

        canvas.drawBitmap(bm,0f,0f,paint)

        val mPath = Path()
        mPath.moveTo(holeRadius, h - pxFromDp(context, holesBottomMargin))
        mPath.quadTo(w - holeRadius, h - pxFromDp(context, holesBottomMargin), w - holeRadius, h - pxFromDp(context, holesBottomMargin))

        val dashed = Paint()
        dashed.setARGB(255, 200, 200, 200)
        dashed.style = Paint.Style.STROKE
        dashed.strokeWidth = 2f
        dashed.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        canvas.drawPath(mPath, dashed)

        super.onDraw(canvas)
    }

    companion object {

        fun pxFromDp(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }
    }
}
