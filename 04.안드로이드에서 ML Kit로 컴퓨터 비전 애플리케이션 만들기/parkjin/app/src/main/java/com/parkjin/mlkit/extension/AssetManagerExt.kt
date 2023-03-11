package com.parkjin.mlkit.extension

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException

fun AssetManager.toBitmap(fileName: String): Bitmap? {
    return try {
        val input = open(fileName)
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        null
    }
}
