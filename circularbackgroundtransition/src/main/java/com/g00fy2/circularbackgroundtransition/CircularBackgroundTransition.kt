package com.g00fy2.circularbackgroundtransition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Suppress("unused")
class CircularBackgroundTransition @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val backgroundView: CircularBackgroundTransitionView

  init {
    backgroundView = CircularBackgroundTransitionView(context)
    addView(backgroundView, ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BACKGROUND_MARGINS, resources.displayMetrics).roundToInt()
      .let { backgroundView.setPadding(it, it, it, it) }
  }

  override fun setBackgroundColor(@ColorInt color: Int) {
    backgroundView.setBackgroundColor(color)
  }

  fun startTransition(@ColorInt nextBackgroundColor: Int) {
    backgroundView.startTransition(nextBackgroundColor)
  }

  private class CircularBackgroundTransitionView(context: Context) : ShapeableImageView(context) {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null
    private var circleTransitionRadius = 0f
    private var fillingRadius = 0f
    private var centerX = 0f
    private var centerY = 0f

    init {
      shapeAppearanceModel = ShapeAppearanceModel.builder(context, null, 0, 0).setAllCorners(
        CornerFamily.ROUNDED,
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, resources.displayMetrics)
      ).build()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
      super.onLayout(changed, left, top, right, bottom)
      centerX = width / 2f
      centerY = height / 2f
      fillingRadius = sqrt((width * width + height * height).toDouble()).toFloat() / 2
    }

    override fun onDraw(canvas: Canvas) {
      canvas.drawCircle(centerX, centerY, circleTransitionRadius, circlePaint)
      super.onDraw(canvas)
    }

    fun startTransition(@ColorInt nextBackgroundColor: Int) {
      if (nextBackgroundColor == circlePaint.color) return
      if (animator?.isRunning == true) animator?.cancel()
      circlePaint.color = nextBackgroundColor

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

    @Keep
    private fun setCircleTransitionRadius(circleTransitionRadius: Float) {
      this.circleTransitionRadius = circleTransitionRadius
      invalidate()
    }
  }

  companion object {
    private const val CORNER_RADIUS = 16f
    private const val BACKGROUND_MARGINS = 16f
    private const val ANIMATION_DURATION = 500
  }
}