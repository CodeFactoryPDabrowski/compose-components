package com.przeman.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.przeman.sample.home.HomeScreen
import com.przeman.sample.progress.ProgressScreen
import com.przeman.sample.stepper.StepperScreen
import com.przeman.sample.theme.SampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SampleTheme {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier.statusBarsPadding(),
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") { HomeScreen(navController) }
                    composable("progress") { ProgressScreen() }
                    composable("stepper") { StepperScreen() }
                }
            }
        }
    }
}
