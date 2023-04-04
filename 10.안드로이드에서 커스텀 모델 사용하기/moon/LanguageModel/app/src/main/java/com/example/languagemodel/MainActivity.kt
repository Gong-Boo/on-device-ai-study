package com.example.languagemodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier

class MainActivity : AppCompatActivity() {

    lateinit var outputText: TextView
    lateinit var inputText: EditText

    lateinit var btnOK: Button
    lateinit var classifier: NLClassifier

    var MODEL_NAME: String = "model.tflite"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        outputText = findViewById(R.id.result_text_view)
        inputText = findViewById(R.id.input_text)
        btnOK = findViewById(R.id.ok_button)
        classifier = NLClassifier.createFromFile(applicationContext, MODEL_NAME);

        btnOK.setOnClickListener {
            val toClassify: String = inputText.text.toString()
            val results: List<Category> = classifier.classify(toClassify)
            showResult(toClassify, results)
        }
    }

    private fun showResult(
        toClassify: String,
        results: List<Category>
    ) {
        runOnUiThread {
            var textToShow = "Input: $toClassify\nOutput:\n"
            for (i in results.indices) {
                val result = results[i]
                textToShow += java.lang.String.format(
                    "    %s: %s\n",
                    result.label,
                    result.score
                )
            }
            textToShow += "---------\n"
            outputText.text = textToShow
        }
    }
}