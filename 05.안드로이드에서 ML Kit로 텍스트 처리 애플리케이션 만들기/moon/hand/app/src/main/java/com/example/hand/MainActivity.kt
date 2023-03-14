package com.example.hand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.RecognitionResult

class MainActivity : AppCompatActivity() {

    private var recognizer: DigitalInkRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeRecognition()

        val doButton = findViewById<Button>(R.id.dobutton)
        doButton.setOnClickListener {
            doRecognize()
        }
    }


    fun initializeRecognition() {
        val modelIdentifier: DigitalInkRecognitionModelIdentifier? =
            DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        val model = DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
        val remoteManager =
            RemoteModelManager.getInstance().download(model, DownloadConditions.Builder().build())

        recognizer = DigitalInkRecognition
            .getClient(DigitalInkRecognizerOptions.builder(model).build())
    }

    private fun doRecognize() {

        val customDrawingSurface = findViewById<CustomDrawingSurface>(R.id.customDrawingSurface)
        val txtOutput = findViewById<TextView>(R.id.txtOutput)

        val thisInk = customDrawingSurface.getInk()

        recognizer?.recognize(thisInk)
            ?.addOnSuccessListener { result: RecognitionResult ->
                var outputString = ""
                txtOutput.text = ""
                for (candidate in result.candidates) {
                    outputString += candidate.text + "\n\n"
                }
                txtOutput.text = outputString
            }
            ?.addOnFailureListener { e: Exception -> }
    }

}