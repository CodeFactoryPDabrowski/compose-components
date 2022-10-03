package com.przeman.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.przeman.sample.progress.ProgressScreen
import com.przeman.sample.stepper.StepperScreen
import com.przeman.sample.theme.SampleTheme
import com.przeman.shared.SizeM

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SampleTheme {
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
        }
    }
}
