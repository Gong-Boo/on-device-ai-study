package com.parkjin.mlkit.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException

fun Context.assetsToBitmap(fileName: String): Bitmap? {
    return try {
        val input = assets.open(fileName)
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        null
    }
}
