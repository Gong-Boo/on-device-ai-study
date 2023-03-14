package com.parkjin.mlkit.reply

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class ReplyView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attributeSet, defStyle) {

    private val txtOutput = TextView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    private val btnReply = Button(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        text = "Generate Reply"
    }

    private val txtInput = EditText(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    init {
        initializeView()
    }

    private fun initializeView() {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = VERTICAL

        addView(txtOutput)
        addView(btnReply)
        addView(txtInput)
    }

    fun setOutputText(text: CharSequence?) {
        txtOutput.text = text
    }

    fun setOnReplyClickListener(block: () -> Unit) {
        btnReply.setOnClickListener { block() }
    }

    fun setInputText(text: CharSequence?) {
        txtInput.setText(text)
    }
}
