package com.przeman.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PreviewBox(
    backgroundColor: Color = Color.Gray,
    content: @Composable () -> Unit
) {
    Box(
        Modifier
            .background(color = backgroundColor)
            .padding(SizeL)
    ) {
        content()
    }
}
