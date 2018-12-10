package com.g00fy2.circularbackgroundtransition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout

class CircularBackgroundTransition constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()
    private var animator: ValueAnimator? = null
    private val layoutCornerRadius: Float
    private var circleTransitionRadius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var layoutWidth: Int = 0
    private var layoutHeight: Int = 0

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        layoutCornerRadius = pxFromDp((LAYOUT_CORNER_RADIUS * 2).toFloat(), context)
        maskPaint.style = Paint.Style.STROKE
        maskPaint.color = Color.WHITE
        maskPaint.strokeWidth = layoutCornerRadius
    }

    override fun onSizeChanged(width: Int, height: Int, oldwidth: Int, oldheight: Int) {
        this.layoutWidth = width
        this.layoutHeight = height
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        var offset = 0f
        if (!LAYOUT_MASK_MARGIN) {
            offset = layoutCornerRadius / 2
        }
        rect.set(-offset, -offset, width + offset, height + offset)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, circleTransitionRadius, circlePaint)
        canvas.drawRoundRect(rect, layoutCornerRadius, layoutCornerRadius, maskPaint)
    }

    fun startTransition(nextBackgroundColor: Int) {
        if (transitionRunning()) {
            animator?.cancel()
        }
        circlePaint.color = nextBackgroundColor
        val fillingRadius =
            Math.sqrt((layoutWidth * layoutWidth + layoutHeight * layoutHeight).toDouble()).toFloat() / 2

        animator =
                ObjectAnimator.ofFloat(this, "circleTransitionRadius", 0f, fillingRadius)?.apply {
                    duration = ANIMATION_DURATION.toLong()
                    interpolator = DecelerateInterpolator()
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            circleTransitionRadius = 0f
                            setBackgroundColor(nextBackgroundColor)
                        }
                    })
                    start()
                }
    }

    fun transitionRunning(): Boolean {
        return animator?.isRunning ?: false
    }

    private fun setCircleTransitionRadius(circleTransitionRadius: Float) {
        this.circleTransitionRadius = circleTransitionRadius
        invalidate()
    }

    private fun pxFromDp(dp: Float, context: Context): Float {
        return dp * context.resources.displayMetrics.density
    }

    companion object {
        private const val LAYOUT_CORNER_RADIUS = 0
        private const val LAYOUT_MASK_MARGIN = false
        private const val ANIMATION_DURATION = 500
    }
}
