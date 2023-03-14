package com.parkjin.mlkit.recognition

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.mlkit.vision.digitalink.Ink
import com.parkjin.mlkit.R

class RecognitionView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attributeSet, defStyle) {

    private val surface = CustomDrawingSurface(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.recognition_surface_height)
        )
    }

    private val txtOutput = TextView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.recognition_txt_output_height)
        )
    }

    private val btnClassify = Button(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        text = "Classify Drawing"
    }

    private val btnClear = Button(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        text = "Clear Drawing"
    }

    init {
        initializeView()
    }

    private fun initializeView() {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = VERTICAL

        addView(surface)
        addView(txtOutput)
        addView(btnClassify)
        addView(btnClear)
    }

    fun getSurfaceInk(): Ink {
        return surface.getInk()
    }

    fun clearSurface() {
        surface.clear()
    }

    fun setOnClassifyClickListener(block: () -> Unit) {
        btnClassify.setOnClickListener { block() }
    }

    fun setOnClearClickListener(block: () -> Unit) {
        btnClear.setOnClickListener { block() }
    }

    fun setOutputText(text: CharSequence?) {
        txtOutput.text = text
    }
}
