package com.parkjin.mlkit.extraction

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.parkjin.mlkit.R

class ExtractionView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attributeSet, defStyle) {

    private val txtInput = EditText(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.extraction_txt_input_height)
        )
        isSingleLine = false
        inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
    }

    private val btnExtract = Button(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        text = "Extract Entities"
    }

    private val txtOutput = TextView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    init {
        initializeView()
    }

    private fun initializeView() {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = VERTICAL

        addView(txtInput)
        addView(btnExtract)
        addView(txtOutput)
    }

    fun getInputText(): String {
        return txtInput.text.toString()
    }

    fun setOnExtractClickListener(block: () -> Unit) {
        btnExtract.setOnClickListener { block() }
    }

    fun setOutputText(text: CharSequence?) {
        txtOutput.text = text
    }
}
