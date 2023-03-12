package com.example.entity_extraction

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.example.entity_extraction.ui.theme.EntityextractionTheme
import com.google.mlkit.nl.entityextraction.*
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val entityExtractor: EntityExtractor by lazy {
        val options = EntityExtractorOptions
            .Builder(EntityExtractorOptions.ENGLISH)
            .build()

        EntityExtraction.getClient(options)
    }

    private var isExtractorAvailable = false

    private val locale = Locale.UK
    private val inputText = mutableStateOf("")
    private val resultText = mutableStateOf("")
    private val onInputChangeListener: (String) -> Unit = { newInputText ->
        inputText.value = newInputText
    }
    private val onButtonClickListener = {
        doExtractor()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EntityextractionTheme {
                MainScreen(
                    inputText = inputText.value,
                    resultText = resultText.value,
                    onInputChangeListener = onInputChangeListener,
                    onButtonClickListener = onButtonClickListener
                )
            }
        }

        prepareExtractor()
    }

    private fun prepareExtractor() {
        entityExtractor
            .downloadModelIfNeeded()
            .addOnSuccessListener {
                isExtractorAvailable = true
            }
            .addOnFailureListener {
                Log.e("TAG", it.message.toString())
            }
    }

    private fun doExtractor() {
        if (isExtractorAvailable.not()) {
            Toast.makeText(this, "Model doesn't downloaded yet", Toast.LENGTH_LONG).show()
            return
        }

        val params = EntityExtractionParams
            .Builder(inputText.value)
            .setPreferredLocale(locale)
            .build()

        entityExtractor
            .annotate(params)
            .addOnSuccessListener { annotations ->
                val result = StringBuffer()

                annotations.map { annotation ->
                    result.append(annotation.annotatedText)

                    annotation.entities.map {
                        result.append("- ${getEntityTypeName(it)}")
                    }

                    result.append("\n\n")
                }

                resultText.value = result.toString()
            }
            .addOnFailureListener {
                Log.e("TAG", it.message.toString())
            }
    }

    private fun getEntityTypeName(entity: Entity): String {
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
            else -> "NOT FOUND"
        }
    }
}
