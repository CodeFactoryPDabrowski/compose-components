package com.przeman.sample.progress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    Progress(value = progressValue, onValueChange = onValueChange)
}
