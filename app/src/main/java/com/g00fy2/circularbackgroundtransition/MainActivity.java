package com.g00fy2.circularbackgroundtransition;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private CircularBackgroundTransition cbt;
  private Random rnd;

  @SuppressLint("ClickableViewAccessibility") @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    cbt = findViewById(R.id.circular_background_transition_view);
    cbt.setOnClickListener(v -> cbt.start(getRandomColor()));
  }

  int getRandomColor() {
    if (rnd == null) rnd = new Random();
    return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
  }
}
