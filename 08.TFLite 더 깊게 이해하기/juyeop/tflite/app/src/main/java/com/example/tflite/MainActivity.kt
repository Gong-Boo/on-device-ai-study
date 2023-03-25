package com.example.tflite

import android.content.res.AssetManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.tflite.ui.theme.TfliteTheme
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class MainActivity : ComponentActivity() {

    private lateinit var interpreter: Interpreter
    private lateinit var model: ByteBuffer

    private var input by mutableStateOf("")
    private val onInputChangeListener: (String) -> Unit = {
        input = it
    }
    private val onButtonClickListener = {
        doInference()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TfliteTheme {
                HomeScreen(
                    input = input,
                    onInputChangeListener = onInputChangeListener,
                    onButtonClickListener = onButtonClickListener
                )
            }
        }

        try {
            model = loadModelFile(assets, "model.tflite")
            interpreter = Interpreter(model)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadModelFile(
        assetManager: AssetManager,
        modelPath: String
    ): ByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun doInference() {
        try {
            val inputArray = floatArrayOf(input.toFloat())
            val outputBuffer = ByteBuffer.allocateDirect(4).apply {
                order(ByteOrder.nativeOrder())
            }

            interpreter.run(inputArray, outputBuffer)
            outputBuffer.rewind()

            val result = outputBuffer.float

            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
