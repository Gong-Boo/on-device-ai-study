package com.example.image_labeling

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val IMG_FILE_NAME = "img_face_test.png"
    }

    private val bitmap: MutableState<Bitmap?> by lazy {
        mutableStateOf(assetsToBitmap(IMG_FILE_NAME))
    }

    private val resultText: MutableState<String> by lazy {
        mutableStateOf("")
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
            imageLabeling.process(image)
                .addOnSuccessListener { labels ->
                    val result = processLabels(labels)
                    resultText.value = result
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

    private fun processLabels(labels: List<ImageLabel>): String {
        val result = StringBuilder()

        labels.map {
            result.append(getString(R.string.result_text, it.text, it.confidence))
        }

        return result.toString()
    }
}
