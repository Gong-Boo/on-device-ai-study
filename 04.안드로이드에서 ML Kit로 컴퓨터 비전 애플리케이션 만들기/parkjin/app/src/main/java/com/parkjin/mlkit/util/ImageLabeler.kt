package com.parkjin.mlkit.util

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object ImageLabeler {

    suspend fun process(bitmap: Bitmap): String {
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(bitmap, 0)

        return suspendCancellableCoroutine { coroutine ->
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    coroutine.resume(
                        labels.joinToString(separator = "\n") { "${it.text}: ${it.confidence}" }
                    )
                }
                .addOnFailureListener(coroutine::resumeWithException)
                .addOnCanceledListener(coroutine::cancel)
        }
    }
}
