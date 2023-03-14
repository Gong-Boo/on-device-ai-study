package com.example.smart_replay

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun MainScreen(
    inputText: String,
    resultText: String,
    onButtonClickListener: () -> Unit
) {
    Column {
        Text(inputText)
        Button(onButtonClickListener) {
            Text("generate replay")
        }
        Text(resultText)
    }
}
