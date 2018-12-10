package com.g00fy2.circularbackgroundtransitionsample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var rnd: Random
    private val randomColor: Int
        get() {
            return rnd.let { Color.argb(255, it.nextInt(256), it.nextInt(256), it.nextInt(256)) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rnd = Random()
        circular_background_transition_view.apply { setOnClickListener { startTransition(randomColor) } }
    }
}
