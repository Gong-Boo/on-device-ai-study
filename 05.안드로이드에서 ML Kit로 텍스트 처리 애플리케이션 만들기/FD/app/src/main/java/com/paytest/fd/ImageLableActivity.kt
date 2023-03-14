package com.paytest.fd

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.paytest.fd.databinding.ActivityImageLableBinding
import java.io.IOException

class ImageLableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_lable)

        val txtOutput : TextView = findViewById(R.id.txtOutput)
        val btn: Button = findViewById(R.id.btnTest)

        val img: ImageView = findViewById(R.id.imageToLabel)
        val fileName = "figure.png"
        val bitmap: Bitmap? = assetsToBitmap(fileName)
        bitmap?.apply {
            img.setImageBitmap(this)
        }


        btn.setOnClickListener {
            // MLKit의 이미지 레이블러를 생성
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            val image = InputImage.fromBitmap(bitmap!!, 0)
            var outputText = ""

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        outputText += "$text : $confidence\n"

                    }
                    labels.joinToString(separator = "\n") { "${it.text} : ${it.confidence}" }
                    txtOutput.text = outputText
                }
                .addOnFailureListener { e ->
                    //TODO 실패했을때 예외처리
                }
        }
    }


    private fun Context.assetsToBitmap(fileName: String): Bitmap?{
        return try {
            with(assets.open(fileName)){
                BitmapFactory.decodeStream(this)
            }
        } catch (e: IOException) { null }
    }
}