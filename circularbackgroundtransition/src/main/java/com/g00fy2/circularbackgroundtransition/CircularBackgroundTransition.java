package com.g00fy2.circularbackgroundtransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

@SuppressWarnings("unused") public class CircularBackgroundTransition extends FrameLayout {

  private final static int LAYOUT_CORNER_RADIUS = 16;
  private final static boolean LAYOUT_MASK_MARGIN = false;
  private final static int ANIMATION_DURATION = 500;

  private final Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private float layoutCornerRadius;
  private RectF rect = new RectF();
  private ValueAnimator animator;
  private float circleTransitionRadius;
  private float centerX;
  private float centerY;
  private int width;
  private int height;

  public CircularBackgroundTransition(Context context) {
    this(context, null);
  }

  public CircularBackgroundTransition(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CircularBackgroundTransition(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    layoutCornerRadius = pxFromDp(LAYOUT_CORNER_RADIUS * 2, context);
    circlePaint.setStyle(Paint.Style.FILL);
    maskPaint.setStyle(Paint.Style.STROKE);
    maskPaint.setColor(Color.WHITE);
    maskPaint.setStrokeWidth(layoutCornerRadius);
  }

  @Override public void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
    this.width = width;
    this.height = height;
    centerX = width / 2;
    centerY = height / 2;
    float offset = 0;
    if (!LAYOUT_MASK_MARGIN) {
      offset = layoutCornerRadius / 2;
    }
    rect.set(-offset, -offset, width + offset, height + offset);
  }

  @Override protected void onDraw(Canvas canvas) {
    canvas.drawCircle(centerX, centerY, circleTransitionRadius, circlePaint);
    canvas.drawRoundRect(rect, layoutCornerRadius, layoutCornerRadius, maskPaint);
  }

  public void start(int nextBackgroundColor) {
    if (animator != null && animator.isRunning()) {
      animator.cancel();
    }
    circlePaint.setColor(nextBackgroundColor);
    float fillingRadius = (float) Math.sqrt(width * width + height * height) / 2;

    animator = ObjectAnimator.ofFloat(this, "circleTransitionRadius", 0, fillingRadius);
    animator.setDuration(ANIMATION_DURATION);
    animator.setInterpolator(new DecelerateInterpolator());
    animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        setBackgroundColor(circlePaint.getColor());
      }
    });
    animator.start();
  }

  private void setCircleTransitionRadius(float circleTransitionRadius) {
    this.circleTransitionRadius = circleTransitionRadius;
    invalidate();
  }

  public boolean transitionRunning() {
    return animator != null && animator.isRunning();
  }

  public float pxFromDp(final float dp, final Context context) {
    return dp * context.getResources().getDisplayMetrics().density;
  }
}
