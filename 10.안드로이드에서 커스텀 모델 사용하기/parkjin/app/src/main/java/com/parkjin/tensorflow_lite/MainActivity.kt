package com.parkjin.tensorflow_lite

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parkjin.tensorflow_lite.flowers_classifier.FlowersClassifierView
import com.parkjin.tensorflow_lite.ml.Model
import org.tensorflow.lite.support.image.TensorImage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFlowersClassifierView(this)
    }

    private fun setFlowersClassifierView(context: Context) {
        val rootView = FlowersClassifierView(context)

        setContentView(rootView)

        rootView.flowers = listOf(
            R.drawable.daisy,
            R.drawable.dandelion,
            R.drawable.roses,
            R.drawable.sunflower,
            R.drawable.tulip,
            R.drawable.tulip2
        ).map { resId ->
            FlowersClassifierView.Item(
                drawableResId = resId,
                onClick = { onClickFlower(it) }
            )
        }
    }

    private fun onClickFlower(view: View) {
        val bitmap = ((view as ImageView).drawable as BitmapDrawable).bitmap
        val model = Model.newInstance(view.context)
        val image = TensorImage.fromBitmap(bitmap)

        val outputs = model.process(image)
        val probability = outputs.probabilityAsCategoryList
        val bestMatch = probability.maxByOrNull { it.score }
        val label = bestMatch?.label

        model.close()
        runOnUiThread { Toast.makeText(this, label, Toast.LENGTH_SHORT).show() }
    }
}
