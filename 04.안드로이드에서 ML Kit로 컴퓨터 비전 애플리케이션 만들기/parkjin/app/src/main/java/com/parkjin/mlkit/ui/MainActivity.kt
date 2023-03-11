package com.parkjin.mlkit.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.parkjin.mlkit.extension.drawStrokeRectangle
import com.parkjin.mlkit.extension.toBitmap
import com.parkjin.mlkit.util.ImageLabeler
import com.parkjin.mlkit.util.ObjectDetector
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootView = MainView(this)
        setContentView(rootView)

        assets.toBitmap(fileName = "city.jpg")
            ?.also {
                val bitmap = it.copy(it.config, true)
                initializeView(rootView, bitmap)
            } ?: finish()
    }

    private fun initializeView(rootView: MainView, bitmap: Bitmap) {
        rootView.setImageBitmap(bitmap)

        rootView.setOnButtonClickListener {
            lifecycleScope.launch {
                val label = ImageLabeler.process(bitmap)
                val objects = ObjectDetector.process(bitmap)

                rootView.setOutputText(label)
                bitmap.drawStrokeRectangle(
                    objects = objects,
                    color = Color.RED,
                    textSize = 32.0f,
                    strokeWidth = 4.0f,
                    isAntiAlias = true
                )
            }
        }
    }
}
