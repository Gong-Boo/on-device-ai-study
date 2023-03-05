package com.example.fd

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource

@Composable
fun MainScreen(
    bitmap: Bitmap?,
    onButtonClickListener: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onButtonClickListener) {
            Text(stringResource(R.string.detect_face))
        }

        bitmap?.asImageBitmap()?.let {
            Image(
                bitmap = it,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}
