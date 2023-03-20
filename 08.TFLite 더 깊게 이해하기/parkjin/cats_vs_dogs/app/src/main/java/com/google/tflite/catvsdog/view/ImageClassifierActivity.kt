package com.google.tflite.catvsdog.view

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.tflite.catvsdog.R
import com.google.tflite.catvsdog.tflite.Classifier

class ImageClassifierActivity : AppCompatActivity() {

    companion object {
        private const val MODEL_PATH = "converted_model.tflite"
        private const val LABEL_PATH = "label.txt"
        private const val INPUT_SIZE = 224
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_classifier)

        val classifier = Classifier(assets, MODEL_PATH, LABEL_PATH, INPUT_SIZE)
        initViews(classifier)
    }

    private fun initViews(classifier: Classifier) {
        val listener: (View?) -> Unit = { view ->
            val bitmap = ((view as ImageView).drawable as BitmapDrawable).bitmap
            val result = classifier.recognizeImage(bitmap)
            runOnUiThread {
                Toast.makeText(this, result.firstOrNull()?.title, Toast.LENGTH_SHORT).show()
            }
        }

        listOf(R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4, R.id.iv_5, R.id.iv_6)
            .map { findViewById<ImageView>(it) }
            .forEach { it.setOnClickListener(listener) }
    }
}
