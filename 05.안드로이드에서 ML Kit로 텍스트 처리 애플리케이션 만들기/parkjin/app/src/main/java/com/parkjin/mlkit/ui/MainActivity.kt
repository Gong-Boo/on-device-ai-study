package com.parkjin.mlkit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parkjin.mlkit.ui.extraction.ExtractionView
import com.parkjin.mlkit.ui.recognition.RecognitionView

class MainActivity : AppCompatActivity() {

    private enum class ViewType {
        EXTRACTION,
        RECOGNITION,
        REPLY;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ViewType.EXTRACTION)
    }

    private fun setContentView(viewType: ViewType) {
        val view = when (viewType) {
            ViewType.EXTRACTION -> ExtractionView(this)
            ViewType.RECOGNITION -> RecognitionView(this)
            ViewType.REPLY -> RecognitionView(this)
        }
        setContentView(view)
    }
}
