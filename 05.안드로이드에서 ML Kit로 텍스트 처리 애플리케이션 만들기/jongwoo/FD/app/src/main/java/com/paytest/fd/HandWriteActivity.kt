package com.paytest.fd

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.Button
import android.widget.TextView
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.*

class HandWriteActivity : AppCompatActivity() {

    lateinit var txtOutput: TextView
    lateinit var customDrawingSurface: CustomDrawingSurface
    lateinit var btnClassify: Button
    lateinit var btnClear: Button
    lateinit var recognizer: DigitalInkRecognizer
    val remoteModelManager = RemoteModelManager.getInstance()
    var model: DigitalInkRecognitionModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hand_write)

        initializeRecognition()
        txtOutput = findViewById(R.id.txtOutput)
        customDrawingSurface = findViewById(R.id.customDrawingSurface)
        btnClassify = findViewById(R.id.btnClassify)
        btnClassify.isEnabled = false
        btnClassify.setOnClickListener {
            val thisInk = customDrawingSurface.getInk()
            recognizer = DigitalInkRecognition.getClient(DigitalInkRecognizerOptions.builder(model!!).build() )
            recognizer.recognize(thisInk)
                .addOnSuccessListener { result: RecognitionResult ->
                    var outputString = ""
                    txtOutput.text = ""
                    for (candidate in result.candidates){
                        outputString+=candidate.text + "\n\n"
                    }
                    txtOutput.text = outputString
                }
                .addOnFailureListener { e: Exception ->
                    Log.e("Digital Ink Test", "Error during recognition: $e")
                }

        }
        btnClear = findViewById(R.id.btnClear)
        btnClear.setOnClickListener {
            customDrawingSurface.clear()
            txtOutput.text = ""
        }
    }

    fun initializeRecognition(){
        val modelIdentifier: DigitalInkRecognitionModelIdentifier? =
            DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        model = DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
        remoteModelManager.download(model!!, DownloadConditions.Builder().build()).addOnSuccessListener {
            Log.i("InkSample", "Model Downloaded")
            btnClassify.isEnabled = true
        }. addOnFailureListener {  e: Exception ->
            Log.e("InkSample", "Model failed $e")
        }
    }
}