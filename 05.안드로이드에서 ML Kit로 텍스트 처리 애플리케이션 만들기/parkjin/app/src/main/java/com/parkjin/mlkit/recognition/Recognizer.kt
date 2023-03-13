package com.parkjin.mlkit.recognition

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Recognizer {

    private val remoteModelManager = RemoteModelManager.getInstance()
    private var recognizer: DigitalInkRecognizer? = null

    private var available: Boolean = false

    suspend fun prepare(languageTag: String = "en-US") {
        val modelIdentifier = DigitalInkRecognitionModelIdentifier
            .fromLanguageTag(languageTag)
            ?: return

        val model = DigitalInkRecognitionModel
            .builder(modelIdentifier)
            .build()

        available = suspendCancellableCoroutine { coroutine ->
            remoteModelManager
                .download(model, DownloadConditions.Builder().build())
                .addOnSuccessListener { coroutine.resume(true) }
                .addOnFailureListener { coroutine.resume(false) }
                .addOnCanceledListener(coroutine::cancel)
        }

        recognizer = DigitalInkRecognition
            .getClient(
                DigitalInkRecognizerOptions
                    .builder(model)
                    .build()
            )
    }

    suspend fun recognize(ink: Ink): String? {
        val recognizer = this.recognizer ?: return null

        return suspendCancellableCoroutine { coroutine ->
            recognizer.recognize(ink)
                .addOnSuccessListener { result ->
                    coroutine.resume(
                        result.candidates.joinToString(separator = "\n\n") { it.text }
                    )
                }
                .addOnFailureListener(coroutine::resumeWithException)
                .addOnCanceledListener(coroutine::cancel)
        }
    }
}
