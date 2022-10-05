package com.przeman.sample.progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.przeman.progress.Progress
import com.przeman.shared.SizeM

@Composable
fun ProgressScreen() {
    val progress = remember { mutableStateOf(0.5f) }
    ProgressContent(progressValue = progress.value, onValueChange = { progress.value = it })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgressContent(
    progressValue: Float, onValueChange: (Float) -> Unit,
) {
    Scaffold(modifier = Modifier
        .systemBarsPadding()
        .fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(modifier = Modifier.padding(horizontal = SizeM), text = "Progress")
        })
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Progress(value = progressValue, onValueChange = onValueChange)
        }
    }
}
