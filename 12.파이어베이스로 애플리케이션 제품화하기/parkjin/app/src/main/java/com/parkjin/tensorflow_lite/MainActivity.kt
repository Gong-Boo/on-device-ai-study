package com.parkjin.tensorflow_lite

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFlowersClassifierView()
    }

    private fun setFlowersClassifierView() {
        val rootView = MainView(this)
        setContentView(rootView)

        loadImageClassifier { imageClassifier ->
            rootView.flowers = listOf(
                R.drawable.daisy,
                R.drawable.dandelion,
                R.drawable.roses,
                R.drawable.sunflower,
                R.drawable.tulip,
                R.drawable.tulip2
            ).map { resId ->
                MainView.Item(
                    drawableResId = resId,
                    onClick = { onClickFlower(it, imageClassifier) }
                )
            }
        }
    }

    private fun onClickFlower(view: View, imageClassifier: ImageClassifier) {
        val bitmap = ((view as ImageView).drawable as BitmapDrawable).bitmap
        val image = TensorImage.fromBitmap(bitmap)

        val results = imageClassifier.classify(image)
        val (label, score) = results.first()
            .categories
            .first()
            .let { it.label to it.score }

        showToast(message = "I see $label with confidence $score")
    }

    private fun loadImageClassifier(onLoaded: (ImageClassifier) -> Unit) {
        lifecycleScope.launch {
            val imageClassifier = getImageClassifier(name = "flowers")
            onLoaded(imageClassifier)
        }
    }

    private suspend fun getImageClassifier(name: String): ImageClassifier =
        suspendCancellableCoroutine { coroutine ->
            val conditions = CustomModelDownloadConditions.Builder()
                .requireWifi()
                .build()

            FirebaseModelDownloader.getInstance()
                .getModel(name, DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
                .addOnSuccessListener { model ->
                    val options = ImageClassifier.ImageClassifierOptions
                        .builder()
                        .setMaxResults(1)
                        .build()

                    val imageClassifier =
                        ImageClassifier.createFromFileAndOptions(model.file, options)
                    coroutine.resume(imageClassifier)
                }
                .addOnFailureListener(coroutine::resumeWithException)
                .addOnCanceledListener(coroutine::cancel)
        }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
