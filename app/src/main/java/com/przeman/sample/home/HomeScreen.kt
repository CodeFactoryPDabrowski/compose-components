package com.przeman.sample.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.przeman.sample.progress.ProgressScreen
import com.przeman.sample.stepper.StepperScreen
import com.przeman.shared.SizeM

@Composable
fun HomeScreen() {
    HomeContent()
}

@Composable
private fun HomeContent() {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(SizeM),
            verticalArrangement = Arrangement.spacedBy(SizeM)
        ) {
            StepperScreen()
            ProgressScreen()
        }
    }
}
