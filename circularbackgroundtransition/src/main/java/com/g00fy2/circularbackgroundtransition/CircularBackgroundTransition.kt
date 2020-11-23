package com.g00fy2.circularbackgroundtransition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import kotlin.math.sqrt

@Suppress("unused")
class CircularBackgroundTransition @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private var animator: ValueAnimator? = null
  private val layoutCornerRadius =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LAYOUT_CORNER_RADIUS, resources.displayMetrics)
  private val transparentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Color.TRANSPARENT
    xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
  }
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var maskBitmap: Bitmap? = null
  private var maskCanvas: Canvas? = null
  private var maskRect = RectF()
  private var circleTransitionRadius = 0f
  private var centerX = 0f
  private var centerY = 0f

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    centerX = width / 2f
    centerY = height / 2f
    if (maskBitmap == null) {
      maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        maskCanvas = Canvas(this)
      }
    }
    maskRect.set(0f, 0f, width.toFloat(), height.toFloat())
  }

  override fun onDraw(canvas: Canvas) {
    canvas.drawCircle(centerX, centerY, circleTransitionRadius, circlePaint)
    maskCanvas!!.drawColor(Color.WHITE)
    maskCanvas!!.drawRoundRect(maskRect, layoutCornerRadius, layoutCornerRadius, transparentPaint)
    canvas.drawBitmap(maskBitmap!!, 0f, 0f, null)
    super.onDraw(canvas)
  }

  fun startTransition(nextBackgroundColor: Int) {
    if (animator != null && animator!!.isRunning) {
      animator!!.cancel()
    }
    circlePaint.color = nextBackgroundColor
    val fillingRadius = sqrt((width * width + height * height).toDouble()).toFloat() / 2

    animator = ObjectAnimator.ofFloat(this, "circleTransitionRadius", 0f, fillingRadius).apply {
      duration = ANIMATION_DURATION.toLong()
      interpolator = DecelerateInterpolator()
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          circleTransitionRadius = 0f
          setBackgroundColor(nextBackgroundColor)
        }
      })
    }
    animator!!.start()
  }

  fun isTransitionRunning() = animator?.isRunning == true

  private fun setCircleTransitionRadius(circleTransitionRadius: Float) {
    this.circleTransitionRadius = circleTransitionRadius
    invalidate()
  }

  companion object {
    private const val LAYOUT_CORNER_RADIUS = 16f
    private const val ANIMATION_DURATION = 500
  }
}
