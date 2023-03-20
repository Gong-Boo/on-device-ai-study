package com.parkjin.tensorflow_lite

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class MainView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attributeSet, defStyle) {

    private val txtLabel = TextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        text = "Enter X: "
    }

    private val txtValue = EditText(context).apply {
        layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT).apply { weight = 1f }
        inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
        setText("1")
    }

    private val btnConvert = Button(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        text = "Convert"
    }

    init {
        initializeView()
    }

    private fun initializeView() {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = HORIZONTAL

        addView(txtLabel)
        addView(txtValue)
        addView(btnConvert)
    }

    fun getValueText(): String {
        return txtValue.text.toString()
    }

    fun setOnConvertClickListener(block: () -> Unit) {
        btnConvert.setOnClickListener { block() }
    }
}
