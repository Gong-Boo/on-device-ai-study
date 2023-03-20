package com.parkjin.tensorflow_lite

import android.content.res.AssetManager
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class TensorflowHelper(
    assetManager: AssetManager,
    modelPath: String
) {

    private val modelFile = loadModelFile(assetManager, modelPath)
    private val interpreter = Interpreter(modelFile)

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel

        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    fun getInferenceValue(inputValue: Float): Float {
        val outputBuffer = ByteBuffer.allocateDirect(4)
        outputBuffer.order(ByteOrder.nativeOrder())

        interpreter.run(floatArrayOf(inputValue), outputBuffer)
        outputBuffer.rewind()

        return outputBuffer.float
    }
}
