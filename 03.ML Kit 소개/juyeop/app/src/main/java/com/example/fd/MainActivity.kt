package com.example.fd

import android.content.Context
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        private const val FILE_NAME = "face-test-3.jpg"
    }

    private val button: Button by lazy {
        findViewById(R.id.btnTest)
    }

    private val imageView: ImageView by lazy {
        findViewById(R.id.imageFace)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bitmap = assetsToBitmap(FILE_NAME)
        bitmap?.apply {
            imageView.setImageBitmap(this)
        }

        button.setOnClickListener {
            val highAccuracyOpts = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build()

            val detector = FaceDetection.getClient(highAccuracyOpts)
            val image = InputImage.fromBitmap(bitmap!!, 0)

            detector.process(image)
                .addOnSuccessListener { faces ->
                    bitmap.apply {
                        imageView.setImageBitmap(drawWithRectangle(faces))
                    }
                }
                .addOnFailureListener { e ->
                    print(e)
                }
        }
    }

    private fun Context.assetsToBitmap(fileName: String): Bitmap? {
        return try {
            with(assets.open(fileName)) {
                BitmapFactory.decodeStream(this)
            }
        } catch (e: IOException) {
            null
        }
    }

    private fun Bitmap.drawWithRectangle(faces: List<Face>): Bitmap? {
        val bitmap = copy(config, true)
        val canvas = Canvas(bitmap)

        faces.forEach { face ->
            val bounds = face.boundingBox

            Paint().apply {
                color = Color.RED
                style = Paint.Style.STROKE
                strokeWidth = 4F
                isAntiAlias = true

                canvas.drawRect(bounds, this)
            }
        }

        return bitmap
    }
}
