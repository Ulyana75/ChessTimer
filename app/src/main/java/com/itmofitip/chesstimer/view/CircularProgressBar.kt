package com.itmofitip.chesstimer.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.itmofitip.chesstimer.R
import kotlin.math.min
import kotlin.math.roundToInt


@SuppressLint("ViewConstructor")
class CircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {
    /**
     * ProgressBar's line thickness
     */
    private var strokeWidth = 4f
    private var progress = 0f
    private var min = 0
    private var max = 100

    /**
     * Start the progress at 12 o'clock
     */
    private val startAngle = -90f
    private var color: Int = Color.DKGRAY
    private var rectF: RectF = RectF()
    private var backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var foregroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0)
        try {
            strokeWidth = typedArray.getDimension(
                R.styleable.CircularProgressBar_progressBarThickness,
                strokeWidth
            )
            progress = typedArray.getFloat(R.styleable.CircularProgressBar_progress, progress)
            color = typedArray.getInt(R.styleable.CircularProgressBar_progressbarColor, color)
            min = typedArray.getInt(R.styleable.CircularProgressBar_min, min)
            max = typedArray.getInt(R.styleable.CircularProgressBar_max, max)
        } finally {
            typedArray.recycle()
        }

        backgroundPaint.color = adjustAlpha(color, 0f)
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = strokeWidth

        foregroundPaint.color = color
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = strokeWidth
    }

    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = min(height, width)
        setMeasuredDimension(min, min)
        rectF.set(
            0 + strokeWidth / 2 + (width - min) / 2,
            0 + strokeWidth / 2 + (height - min) / 2,
            min - strokeWidth / 2 + (width - min) / 2,
            min - strokeWidth / 2 + (height - min) / 2
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawOval(rectF, backgroundPaint)
        val angle = 360 * progress / max
        canvas?.drawArc(rectF, startAngle, angle, false, foregroundPaint)
    }

    fun setProgress(newProgress: Float) {
        progress = newProgress
        invalidate()
    }

    fun setColor(newColor: Int) {
        color = newColor
        foregroundPaint.color = color
        invalidate()
    }
}