package com.parkjin.mlkit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.parkjin.mlkit.extension.assetsToBitmap
import com.parkjin.mlkit.extension.drawStrokeRectangle
import com.parkjin.mlkit.util.FaceProcessor
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val FILE_NAME = "face-test.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootView = MainView(this)
        setContentView(rootView)
        initialize(rootView)
    }

    private fun initialize(rootView: MainView) {
        val bitmap = assetsToBitmap(FILE_NAME)
            ?.let { it.copy(it.config, true) }
            ?: return

        rootView.setImageBitmap(bitmap)
        rootView.setOnButtonClickListener {
            lifecycleScope.launch {
                val faces = FaceProcessor.process(bitmap, FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                bitmap.drawStrokeRectangle(faces)
            }
        }
    }
}
