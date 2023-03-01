package com.parkjin.mlkit.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.mlkit.vision.face.Face

fun Bitmap.drawStrokeRectangle(
    faces: List<Face>,
    color: Int = Color.RED,
    strokeWidth: Float = 4.0f,
    isAntiAlias: Boolean = true
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