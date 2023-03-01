package com.parkjin.mlkit.util

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FaceProcessor {

    suspend fun process(
        bitmap: Bitmap,
        @FaceDetectorOptions.PerformanceMode performanceMode: Int
    ): List<Face> {
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(performanceMode)
            .build()

        val detector = FaceDetection.getClient(highAccuracyOpts)
        val image = InputImage.fromBitmap(bitmap, 0)

        return suspendCancellableCoroutine { coroutine ->
            detector.process(image)
                .addOnSuccessListener(coroutine::resume)
                .addOnFailureListener(coroutine::resumeWithException)
                .addOnCanceledListener(coroutine::cancel)
        }
    }
}
