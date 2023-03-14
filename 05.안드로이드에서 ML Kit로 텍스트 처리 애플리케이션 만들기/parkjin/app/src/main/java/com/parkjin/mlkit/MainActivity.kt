package com.parkjin.mlkit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.nl.smartreply.TextMessage
import com.parkjin.mlkit.extraction.ExtractionView
import com.parkjin.mlkit.extraction.Extractor
import com.parkjin.mlkit.recognition.RecognitionView
import com.parkjin.mlkit.recognition.Recognizer
import com.parkjin.mlkit.reply.ReplyGenerator
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

        val view = getContentView(ViewType.REPLY)
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
        val extractor = Extractor()

        lifecycleScope.launch { extractor.prepare() }

        view.setOnExtractClickListener {
            lifecycleScope.launch {
                val output = extractor.extract(view.getInputText())
                view.setOutputText(output)
            }
        }
    }

    private fun setRecognitionView(view: RecognitionView) {
        val recognizer = Recognizer()

        lifecycleScope.launch { recognizer.prepare() }

        view.setOnClassifyClickListener {
            lifecycleScope.launch {
                val output = recognizer.recognize(view.getSurfaceInk())
                view.setOutputText(output)
            }
        }

        view.setOnClearClickListener {
            view.clearSurface()
            view.setOutputText("")
        }
    }

    private fun setReplyView(view: ReplyView) {
        val replyGenerator = ReplyGenerator()

        val myName = "Me"
        val youName = "Jin"

        val messages = listOf(
            "Hi, good morning!" to myName,
            "Oh, hey -- how are you?" to youName,
            "Just got up, thinking of heading out for breakfast" to myName,
            "Want to meet up?" to youName,
            "Sure, what do you fancy?" to myName,
            "Just coffee, or do you want to eat?" to youName
        )

        val items = messages.map { (message, sender) ->
            val isMine = sender == myName
            if (isMine) {
                TextMessage.createForLocalUser(message, System.currentTimeMillis())
            } else {
                TextMessage.createForRemoteUser(message, System.currentTimeMillis(), sender)
            }
        }

        view.setOutputText(
            messages.joinToString(separator = "\n") { (message, sender) -> "$sender : $message" }
        )

        view.setOnReplyClickListener {
            lifecycleScope.launch {
                view.setInputText(replyGenerator.generate(items))
            }
        }
    }
}
