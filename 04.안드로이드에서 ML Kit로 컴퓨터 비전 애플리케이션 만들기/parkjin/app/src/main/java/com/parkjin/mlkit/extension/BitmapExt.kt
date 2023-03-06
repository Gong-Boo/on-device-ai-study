package com.parkjin.mlkit.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.google.mlkit.vision.objects.DetectedObject

fun Bitmap.drawStrokeRectangle(
    objects: List<DetectedObject>,
    @ColorInt color: Int,
    textSize: Float,
    strokeWidth: Float,
    isAntiAlias: Boolean
): Bitmap = apply {
    val canvas = Canvas(this)

    val paint = Paint().apply {
        style = Paint.Style.STROKE
        this.color = color
        this.textSize = textSize
        this.strokeWidth = strokeWidth
        this.isAntiAlias = isAntiAlias
    }

    objects.forEachIndexed { index, detectedObject ->
        val bounds = detectedObject.boundingBox

        canvas.drawRect(bounds, paint)
        canvas.drawText(index.toString(), bounds.left.toFloat(), bounds.top.toFloat(), paint)
    }
}
