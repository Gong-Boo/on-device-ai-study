package com.parkjin.mlkit.util

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object ObjectDetector {

    suspend fun process(bitmap: Bitmap): List<DetectedObject> {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .build()

        val detector = ObjectDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap, 0)

        return suspendCancellableCoroutine { coroutine ->
            detector.process(image)
                .addOnSuccessListener(coroutine::resume)
                .addOnFailureListener(coroutine::resumeWithException)
                .addOnCanceledListener(coroutine::cancel)
        }
    }
}
