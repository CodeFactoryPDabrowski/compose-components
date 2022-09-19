package com.przeman.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.przeman.stepper.Stepper
import com.przeman.stepper.itemsIndexed

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Box(modifier = Modifier.statusBarsPadding()) {
                Stepper(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    lineColor = Color.Black
                ) {
                    itemsIndexed(
                        items = buildList {
                            add(StepperItem("Title1"))
                            add(StepperItem("Title2"))
                            add(StepperItem("Title3"))
                            add(StepperItem("Title4"))
                            add(StepperItem("Title5"))
                        },
                        itemContent = { _, item ->
                            Text(text = item.title)
                        },
                        indicatorContent = { index, _ ->
                            StepperIndicator(text = index.inc().toString())
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StepperIndicator(text: String) {
    Box(
        Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color.White, CircleShape)
            .border(1.dp, Color.Black, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text)
    }
}

data class StepperItem(val title: String)
