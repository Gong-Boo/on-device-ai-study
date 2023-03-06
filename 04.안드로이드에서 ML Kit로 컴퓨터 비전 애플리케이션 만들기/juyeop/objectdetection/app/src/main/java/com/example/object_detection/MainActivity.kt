package com.example.object_detection

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val IMG_FILE_NAME = "img_bird_test.png"
    }

    private val bitmap: MutableState<Bitmap?> by lazy {
        mutableStateOf(assetsToBitmap(IMG_FILE_NAME))
    }

    private val resultText: MutableState<String> by lazy {
        mutableStateOf("")
    }

    private val objectDetector: ObjectDetector by lazy {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        ObjectDetection.getClient(options)
    }

    private val imageLabeling: ImageLabeler by lazy {
        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.3F)
            .build()

        ImageLabeling.getClient(options)
    }

    private val inputImage: InputImage? by lazy {
        bitmap.value?.let {
            InputImage.fromBitmap(it, 0)
        }
    }

    private val onButtonClickListener: () -> Unit = {
        inputImage?.let { image ->
            objectDetector.process(image)
                .addOnSuccessListener { detectedObjects ->
                    val resultBitmap = bitmap.value?.drawDetectedObjects(detectedObjects)
                    bitmap.value = resultBitmap
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                bitmap = bitmap.value,
                resultText = resultText.value,
                onButtonClickListener = onButtonClickListener
            )
        }
    }

    private fun Context.assetsToBitmap(fileName: String): Bitmap? {
        return try {
            assets.open(fileName).run {
                BitmapFactory.decodeStream(this)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            null
        }
    }

    private fun Bitmap.drawDetectedObjects(objects: List<DetectedObject>): Bitmap {
        val resultBitmap = copy(config, true)
        val canvas = Canvas(resultBitmap)
        var thisLabel = 0

        objects.map {
            val bounds = it.boundingBox

            thisLabel++

            Paint().apply {
                color = Color.RED
                style = Paint.Style.STROKE
                textSize = 32.0F
                strokeWidth = 4.0F
                isAntiAlias = true

                canvas.drawRect(
                    bounds,
                    this
                )

                canvas.drawText(
                    thisLabel.toString(),
                    bounds.left.toFloat(),
                    bounds.top.toFloat(),
                    this
                )
            }
        }

        processLabels(resultBitmap, objects)

        return resultBitmap
    }

    private fun processLabels(
        bitmap: Bitmap,
        objects: List<DetectedObject>
    ) {
        objects.map { detectObject ->
            val bounds = detectObject.boundingBox
            val croppedBitmap = Bitmap.createBitmap(
                bitmap,
                bounds.left,
                bounds.top,
                bounds.width(),
                bounds.height()
            )
            val image = InputImage.fromBitmap(croppedBitmap, 0)

            imageLabeling.process(image)
                .addOnSuccessListener { labels ->
                    val result = StringBuilder(resultText.value)
                    labels.map {
                        result.append("${it.text} |")
                    }
                    result.append("\n\n")
                    resultText.value = result.toString()
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        }
    }
}