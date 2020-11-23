package com.g00fy2.circularbackgroundtransitionsample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g00fy2.circularbackgroundtransition.CircularBackgroundTransition
import com.g00fy2.circularbackgroundtransitionsample.R.id
import com.g00fy2.circularbackgroundtransitionsample.R.layout
import java.util.Random

class MainActivity : AppCompatActivity() {

  private val random by lazy { Random() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
    findViewById<CircularBackgroundTransition>(id.circular_background_transition_view)?.run {
      setBackgroundColor(randomColor())
      setOnClickListener { startTransition(randomColor()) }
    }
  }

  private fun randomColor() = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
}