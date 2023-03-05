package com.parkjin.mlkit.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.google.mlkit.vision.face.Face

fun Bitmap.drawStrokeRectangle(
    faces: List<Face>,
    @ColorInt color: Int,
    strokeWidth: Float,
    isAntiAlias: Boolean
): Bitmap = apply {
    val canvas = Canvas(this)
    val paint = Paint().apply {
        style = Paint.Style.STROKE
        this.color = color
        this.strokeWidth = strokeWidth
        this.isAntiAlias = isAntiAlias
    }

    faces.forEach { face -> canvas.drawRect(face.boundingBox, paint) }
}
