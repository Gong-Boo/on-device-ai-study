package com.parkjin.mlkit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.parkjin.mlkit.extraction.ExtractionView
import com.parkjin.mlkit.extraction.Extractor
import com.parkjin.mlkit.recognition.RecognitionView
import com.parkjin.mlkit.reply.ReplyView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private enum class ViewType {
        EXTRACTION,
        RECOGNITION,
        REPLY;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = getContentView(ViewType.EXTRACTION)
        setContentView(view)
        initializeView(view)
    }

    private fun getContentView(viewType: ViewType): View {
        return when (viewType) {
            ViewType.EXTRACTION -> ExtractionView(this)
            ViewType.RECOGNITION -> RecognitionView(this)
            ViewType.REPLY -> ReplyView(this)
        }
    }

    private fun initializeView(view: View) {
        when (view) {
            is ExtractionView -> setExtractionView(view)
            is RecognitionView -> setRecognitionView(view)
            is ReplyView -> setReplyView(view)
            else -> return finish()
        }
    }

    private fun setExtractionView(view: ExtractionView) {
        lifecycleScope.launch { Extractor.prepare() }

        view.setOnExtractClickListener {
            lifecycleScope.launch {
                val output = Extractor.extract(view.getInputText())
                view.setOutputText(output)
            }
        }
    }

    private fun setRecognitionView(view: RecognitionView) {
    }

    private fun setReplyView(view: ReplyView) {
    }
}
