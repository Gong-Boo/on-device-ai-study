package com.parkjin.tensorflow_lite

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tensorflowHelper = TensorflowHelper(assets, "model.tflite")

        val rootView = MainView(this)
        setContentView(rootView)

        rootView.setOnConvertClickListener {
            rootView.getValueText()
                .toFloatOrNull()
                ?.let(tensorflowHelper::getInferenceValue)
                ?.let(::showAlertDialog)
        }
    }

    private fun showAlertDialog(inference: Float) {
        AlertDialog.Builder(this)
            .apply {
                setTitle("tensorflow-lite interpreter")
                setMessage("value is: $inference")
                setNeutralButton("close") { dialog, _ -> dialog.cancel() }
            }
            .show()
    }
}
