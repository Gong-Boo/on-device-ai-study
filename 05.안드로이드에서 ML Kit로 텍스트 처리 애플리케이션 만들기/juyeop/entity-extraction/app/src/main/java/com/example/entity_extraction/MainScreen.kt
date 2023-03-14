package com.example.entity_extraction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(
    inputText: String,
    resultText: String,
    onInputChangeListener: (String) -> Unit,
    onButtonClickListener: () -> Unit
) {
    Column {
        TextField(
            value = inputText,
            onValueChange = onInputChangeListener,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onButtonClickListener) {
            Text("extract entities")
        }
        Text(resultText)
    }
}
