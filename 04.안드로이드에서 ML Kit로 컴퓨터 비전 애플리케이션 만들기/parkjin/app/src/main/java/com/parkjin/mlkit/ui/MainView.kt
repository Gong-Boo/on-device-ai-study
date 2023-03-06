package com.parkjin.mlkit.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attributeSet, defStyle) {

    private val imgToLabel = ImageView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        gravity = Gravity.CENTER
        adjustViewBounds = true
    }

    private val btnTest = Button(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        text = "Label Image"
        gravity = Gravity.CENTER
    }

    private val txtOutput = TextView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        setEms(10)
        gravity = Gravity.START or Gravity.TOP
    }

    init {
        initializeView()
    }

    private fun initializeView() {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = VERTICAL

        addView(imgToLabel)
        addView(btnTest)
        addView(txtOutput)
    }

    fun setImageBitmap(bitmap: Bitmap) {
        imgToLabel.setImageBitmap(bitmap)
    }

    fun setOnButtonClickListener(block: () -> Unit) {
        btnTest.setOnClickListener { block() }
    }

    fun setOutputText(text: CharSequence) {
        txtOutput.text = text
    }
}
