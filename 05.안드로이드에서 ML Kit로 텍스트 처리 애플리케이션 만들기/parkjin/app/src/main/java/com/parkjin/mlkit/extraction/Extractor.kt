package com.parkjin.mlkit.extraction

import com.google.mlkit.nl.entityextraction.Entity
import com.google.mlkit.nl.entityextraction.EntityExtraction
import com.google.mlkit.nl.entityextraction.EntityExtractionParams
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Extractor(private val onAvailableChange: (Boolean) -> Unit) {

    private val entityExtractor = EntityExtraction.getClient(
        EntityExtractorOptions
            .Builder(EntityExtractorOptions.ENGLISH)
            .build()
    )

    private var available: Boolean = false
        set(value) {
            field = value
            onAvailableChange(value)
        }

    init {
        available = false
    }

    suspend fun prepare() {
        available = suspendCancellableCoroutine { coroutine ->
            entityExtractor.downloadModelIfNeeded()
                .addOnSuccessListener { coroutine.resume(true) }
                .addOnFailureListener { coroutine.resume(false) }
                .addOnCanceledListener(coroutine::cancel)
        }
    }

    suspend fun extract(input: String, locale: Locale = Locale.US): String? {
        if (!available) return null

        val params = EntityExtractionParams.Builder(input)
            .setPreferredLocale(locale)
            .build()

        return suspendCancellableCoroutine { coroutine ->
            entityExtractor.annotate(params)
                .addOnSuccessListener { annotations ->
                    coroutine.resume(
                        annotations.map { annotation ->
                            annotation.entities.map { entity ->
                                "${annotation.annotatedText} : Type - ${formatEntityType(entity)}"
                            }
                        }.joinToString(separator = "\n\n")
                    )
                }
                .addOnFailureListener(coroutine::resumeWithException)
                .addOnCanceledListener(coroutine::cancel)
        }
    }

    private fun formatEntityType(entity: Entity): String {
        return when (entity.type) {
            Entity.TYPE_ADDRESS -> "Address"
            Entity.TYPE_DATE_TIME -> "DateTime"
            Entity.TYPE_EMAIL -> "Email Address"
            Entity.TYPE_FLIGHT_NUMBER -> "Flight Number"
            Entity.TYPE_IBAN -> "IBAN"
            Entity.TYPE_ISBN -> "ISBN"
            Entity.TYPE_MONEY -> "Money"
            Entity.TYPE_PAYMENT_CARD -> "Credit/Debit Card"
            Entity.TYPE_PHONE -> "Phone Number"
            Entity.TYPE_TRACKING_NUMBER -> "Tracking Number"
            Entity.TYPE_URL -> "URL"
            else -> "Address"
        }
    }
}
