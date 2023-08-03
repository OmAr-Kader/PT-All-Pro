package com.pt.common.moderator.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.DecelerateInterpolator
import androidx.core.content.res.use
import com.pt.common.R
import kotlin.math.roundToInt

open class CircleProgressBar @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.progressBarStyleLarge,
) : View(context, attrs, defStyle), CircleProgressListener {

    /**
     * ProgressBar's line thickness
     */
    private var strokeW = 4f
    private var progressCir = 0f
    private var minCir = 0
    private var maxCir = 100

    /**
     * Start the progress at 12 o'clock
     */
    private val startAngle = -90
    private var colorDef = Color.DKGRAY
    private var rectF: RectF? = null
    private var backgroundPaint: Paint? = null
    private var foregroundPaint: Paint? = null

    init {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        rectF = RectF()
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.CircleProgressBar,
            0, 0
        )?.use {
            strokeW = it.getDimension(R.styleable.CircleProgressBar_progressBarThickness, strokeW)
            progressCir = it.getFloat(R.styleable.CircleProgressBar_progress, progressCir)
            colorDef = it.getInt(R.styleable.CircleProgressBar_progressbarColor, colorDef)
            minCir = it.getInt(R.styleable.CircleProgressBar_min, minCir)
            maxCir = it.getInt(R.styleable.CircleProgressBar_max, maxCir)
        }
        //Reading values from the XML layout

        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint?.apply {
            color = adjustAlpha(colorDef, 0.3f)
            style = Paint.Style.STROKE
            strokeWidth = strokeW
        }

        foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        foregroundPaint?.apply {
            color = colorDef
            style = Paint.Style.STROKE
            strokeWidth = strokeW
        }
    }

    override fun getStrokeWidth(): Float {
        return strokeW
    }

    override fun setStrokeWidth(strokeWidth: Float) {
        this.strokeW = strokeWidth
        (backgroundPaint ?: return).strokeWidth = strokeWidth
        (foregroundPaint ?: return).strokeWidth = strokeWidth
        invalidate()
        requestLayout() //Because it should recalculate its bounds
    }

    override fun getProgress(): Float {
        return progressCir
    }

    override fun setProgress(progress: Float) {
        this.progressCir = progress
        invalidate()
    }

    override fun getMin(): Int {
        return minCir
    }

    override fun setMin(min: Int) {
        this.minCir = min
        invalidate()
    }

    override fun getMax(): Int {
        return maxCir
    }

    override fun setMax(max: Int) {
        this.maxCir = max
        invalidate()
    }

    override fun getColor(): Int {
        return colorDef
    }

    override fun setColor(color: Int) {
        this.colorDef = color
        (backgroundPaint ?: return).color = adjustAlpha(color, 0.3f)
        (foregroundPaint ?: return).color = color
        invalidate()
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawOval(rectF ?: return, backgroundPaint ?: return)
        val angle = 360 * progressCir / maxCir
        canvas.drawArc(
            rectF ?: return,
            startAngle.toFloat(),
            angle,
            false,
            foregroundPaint ?: return
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = width.coerceAtMost(height)
        setMeasuredDimension(min, min)
        (rectF ?: return)[0 + strokeW / 2, 0 + strokeW / 2, min - strokeW / 2] =
            min - strokeW / 2
    }

    /**
     * Lighten the given color by the factor
     *
     * @param color  The color to lighten
     * @param factor 0 to 4
     * @return A brighter color
     */
    override fun lightenColor(color: Int, factor: Float): Int {
        val r = Color.red(color) * factor
        val g = Color.green(color) * factor
        val b = Color.blue(color) * factor
        val ir = 255.coerceAtMost(r.toInt())
        val ig = 255.coerceAtMost(g.toInt())
        val ib = 255.coerceAtMost(b.toInt())
        val ia = Color.alpha(color)
        return Color.argb(ia, ir, ig, ib)
    }

    /**
     * Transparent the given color by the factor
     * The more the factor closer to zero the more the color gets transparent
     *
     * @param color  The color to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted color
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    /**
     * Set the progress with an animation.
     * Note that the [android.animation.ObjectAnimator] Class automatically set the progress
     * so don't call the [CircleProgressBar.setProgress] directly within this method.
     *
     * @param progress The progress it should animate to it.
     */
    override fun setProgressWithAnimation(progress: Float) {
        androidx.core.animation.ObjectAnimator.ofFloat(
            this,
            com.pt.common.BuildConfig.PROGRESS_CONST,
            progress
        ).apply {
            duration = 1500
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    fun onViewDestroy() {
        rectF = null
        backgroundPaint = null
        foregroundPaint = null
    }

}