package com.przeman.sample.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.przeman.shared.SizeM

@Composable
fun HomeScreen(navController: NavController) {
    HomeContent { screen -> navController.navigate(screen) }
}

@Composable
private fun HomeContent(onNavigationChange: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(SizeM),
            verticalArrangement = Arrangement.spacedBy(SizeM),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextButton(text = "Stepper") {
                onNavigationChange("stepper")
            }
            TextButton(text = "Progress") {
                onNavigationChange("progress")
            }
        }
    }
}

@Composable
private fun TextButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(), onClick = onClick
    ) {
        Text(text = text)
    }
}
