package com.przeman.sample.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.przeman.progress.Progress

@Composable
fun ProgressScreen() {
    val progress = remember { mutableStateOf(0.5f) }
    ProgressContent(progressValue = progress.value, onValueChange = { progress.value = it })
}

@Composable
private fun ProgressContent(
    progressValue: Float, onValueChange: (Float) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Progress(value = progressValue, onValueChange = onValueChange)
    }
}
