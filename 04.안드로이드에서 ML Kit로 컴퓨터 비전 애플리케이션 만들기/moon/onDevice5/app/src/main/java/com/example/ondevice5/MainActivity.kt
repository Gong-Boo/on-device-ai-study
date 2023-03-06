package com.example.ondevice5

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val fileName = "bird.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val options =
            ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .build()


        val img: ImageView = findViewById(R.id.imageToLabel)
        val bitmap: Bitmap? = assetsToBitmap(fileName)
        bitmap?.apply {
            img.setImageBitmap(this)
        }

        val txtOutput: TextView = findViewById(R.id.txtOutput)
        val btn: Button = findViewById(R.id.btnTest)

        btn.setOnClickListener {
            val objectDetector = ObjectDetection.getClient(options)
            val image = InputImage.fromBitmap(bitmap!!, 0)
            objectDetector.process(image)
                .addOnSuccessListener { detectedObjects -> // 성공적으로 태스크가 끝나면
                    bitmap.apply {
                        img.setImageBitmap(drawWithRectangle(detectedObjects))
                        getLabels(bitmap, detectedObjects, txtOutput)
                    }
                }
                .addOnFailureListener { e ->

                }
        }
    }


}

fun Context.assetsToBitmap(fileName: String): Bitmap? {
    return try {
        with(assets.open(fileName)) {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {
        null
    }
}

fun Bitmap.drawWithRectangle(objects: List<DetectedObject>): Bitmap? {
    val bitmap = copy(config, true)
    val canvas = Canvas(bitmap)
    var thisLabel = 0
    for (obj in objects) {
        thisLabel++
        val bounds = obj.boundingBox
        Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            textSize = 32.0f
            strokeWidth = 4.0f
            isAntiAlias = true
            canvas.drawRect(
                bounds,
                this
            )
            canvas.drawText(
                thisLabel.toString(),
                bounds.left.toFloat(),
                bounds.top.toFloat(), this
            )
        }
    }
    return bitmap
}

fun getLabels(
    bitmap: Bitmap,
    objects: List<DetectedObject>, txtOutput: TextView
) {
    val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
    for (obj in objects) {
        val bounds = obj.boundingBox
        val croppedBitmap = Bitmap.createBitmap(
            bitmap,
            bounds.left,
            bounds.top,
            bounds.width(),
            bounds.height()
        )
        val image = InputImage.fromBitmap(croppedBitmap!!, 0)
        labeler.process(image)
            .addOnSuccessListener { labels ->
                var labelText = ""
                if (labels.isNotEmpty()) {
                    labelText = txtOutput.text.toString()
                    for (thisLabel in labels) {
                        labelText += thisLabel.text + " , "
                    }
                    labelText += "\n"
                } else {
                    labelText = "Not found." + "\n"
                }
                txtOutput.text = labelText
            }
    }
}
