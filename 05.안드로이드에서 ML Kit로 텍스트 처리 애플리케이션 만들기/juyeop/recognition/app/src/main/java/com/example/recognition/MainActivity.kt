package com.example.recognition

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions

class MainActivity : AppCompatActivity() {

    private val cvSurface: CustomDrawingSurface by lazy {
        findViewById(R.id.cv_surface)
    }
    private val btnClassify: Button by lazy {
        findViewById(R.id.btn_classify)
    }
    private val btnClear: Button by lazy {
        findViewById(R.id.btn_clear)
    }
    private val tvResult: TextView by lazy {
        findViewById(R.id.tv_result)
    }

    private var recognition: DigitalInkRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnClassify.setOnClickListener {
            doRecognize()
        }
        btnClear.setOnClickListener {
            cvSurface.clear()
        }

        initRecognition()
    }

    private fun initRecognition() {
        val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US") // option
        val model = DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
        val remoteModelManager = RemoteModelManager.getInstance()

        remoteModelManager
            .download(model, DownloadConditions.Builder().build())
            .addOnFailureListener { e: Exception ->
                Log.e("TAG", e.message.toString())
            }

        recognition = DigitalInkRecognition
            .getClient(DigitalInkRecognizerOptions.builder(model).build())
    }

    private fun doRecognize(){
        recognition
            ?.recognize(cvSurface.getInk())
            ?.addOnSuccessListener { result ->
                val str = StringBuilder()

                result.candidates.map { candidate ->
                    str.append("${candidate.text} - ${candidate.score}")
                    str.append("\n")
                }

                tvResult.text = str.toString()
            }
            ?.addOnFailureListener {
                Log.e("TAG", it.message.toString())
            }
    }
}
