package com.parkjin.mlkit.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.parkjin.mlkit.extension.assetsToBitmap
import com.parkjin.mlkit.extension.drawStrokeRectangle
import com.parkjin.mlkit.util.FaceProcessor
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootView = MainView(this)
        setContentView(rootView)

        assetsToBitmap(fileName = "face-test.jpg")
            ?.also {
                val bitmap = it.copy(it.config, true)
                initializeView(rootView, bitmap)
            } ?: finish()
    }

    private fun initializeView(rootView: MainView, bitmap: Bitmap) {
        rootView.setImageBitmap(bitmap)

        rootView.setOnButtonClickListener {
            lifecycleScope.launch {
                val faces = FaceProcessor.process(bitmap, FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                bitmap.drawStrokeRectangle(
                    faces = faces,
                    color = Color.RED,
                    strokeWidth = 4.0f,
                    isAntiAlias = true
                )
            }
        }
    }

}
