package com.g00fy2.circularbackgroundtransition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.AccelerateDecelerateInterpolator
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
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var circleTransitionRadius = 0f
  private var centerX = 0f
  private var centerY = 0f

  init {
    outlineProvider = object : ViewOutlineProvider() {
      override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(0, 0, view.width, view.height, layoutCornerRadius)
      }
    }
    clipToOutline = true
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    centerX = width / 2f
    centerY = height / 2f
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.drawCircle(centerX, centerY, circleTransitionRadius, circlePaint)
  }

  fun startTransition(nextBackgroundColor: Int) {
    if (animator != null && animator!!.isRunning) {
      animator!!.cancel()
    }
    circlePaint.color = nextBackgroundColor
    val fillingRadius = sqrt((width * width + height * height).toDouble()).toFloat() / 2

    animator = ObjectAnimator.ofFloat(this, "circleTransitionRadius", 0f, fillingRadius).apply {
      duration = ANIMATION_DURATION.toLong()
      interpolator = AccelerateDecelerateInterpolator()
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
