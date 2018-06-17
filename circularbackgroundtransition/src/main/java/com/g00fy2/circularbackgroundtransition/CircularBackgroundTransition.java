package com.g00fy2.circularbackgroundtransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

@SuppressWarnings("unused") public class CircularBackgroundTransition extends View {

  private Paint paint;
  private ValueAnimator animator;
  private int width;
  private int height;
  private float centerX;
  private float centerY;
  private float radius;

  public CircularBackgroundTransition(Context context) {
    this(context, null);
  }

  public CircularBackgroundTransition(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CircularBackgroundTransition(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    paint = new Paint();
    paint.setStyle(Paint.Style.FILL);
    paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
  }

  @Override public void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
    this.width = width;
    this.height = height;
    centerX = width / 2;
    centerY = height / 2;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawCircle(centerX, centerY, radius, paint);
  }

  public void start(int nextBackgroundColor) {
    if (animator != null && animator.isRunning()) {
      animator.cancel();
    }
    paint.setColor(nextBackgroundColor);
    float fillingRadius = (float) Math.sqrt(width * width + height * height) / 2;

    animator = ObjectAnimator.ofFloat(this, "radius", 0, fillingRadius);
    animator.setDuration(400);
    animator.setInterpolator(new LinearInterpolator());
    animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        setBackgroundColor(paint.getColor());
      }
    });
    animator.start();
  }

  public boolean transitionRunning() {
    return animator == null || !animator.isRunning();
  }

  public void setRadius(float radius) {
    this.radius = radius;
    invalidate();
  }
}
