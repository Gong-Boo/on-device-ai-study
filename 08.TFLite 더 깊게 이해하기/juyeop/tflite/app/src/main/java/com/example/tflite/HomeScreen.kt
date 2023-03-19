package com.example.tflite

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    input: String,
    onInputChangeListener: (String) -> Unit,
    onButtonClickListener: () -> Unit
) {
    Row {
        Text("x 좌표")
        TextField(value = input, onValueChange = onInputChangeListener)
        Button(onClick = onButtonClickListener) {
            Text("실행")
        }
    }
}