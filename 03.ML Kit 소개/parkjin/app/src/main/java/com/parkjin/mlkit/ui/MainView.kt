package com.parkjin.mlkit.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.parkjin.mlkit.R

class MainView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    private val button = Button(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        text = context.getString(R.string.button_text)
    }

    private val image = ImageView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    init {
        initializeView()
    }

    private fun initializeView() {
        addView(button)
        addView(image)
    }

    fun setOnButtonClickListener(block: () -> Unit) {
        button.setOnClickListener { block() }
    }

    fun setImageBitmap(bitmap: Bitmap) {
        image.setImageBitmap(bitmap)
    }
}
