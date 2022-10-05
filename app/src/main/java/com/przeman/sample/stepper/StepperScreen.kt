package com.przeman.sample.stepper

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.przeman.shared.SizeM
import com.przeman.stepper.Stepper
import com.przeman.stepper.items
import com.przeman.stepper.itemsIndexed

@Composable
fun StepperScreen() {
    StepperContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepperContent() {
    Scaffold(modifier = Modifier
        .systemBarsPadding()
        .fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(modifier = Modifier.padding(horizontal = SizeM), text = "Stepper")
        })
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Stepper {
                itemsIndexed(items = buildList {
                    add(StepperItem("Title1"))
                    add(StepperItem("Title2"))
                    add(StepperItem("Title3"))
                    add(StepperItem("Title4"))
                    add(StepperItem("Title5"))
                }, itemContent = { _, item ->
                    Text(text = item.title)
                }, indicatorContent = { index, _ ->
                    StepperIndicator(
                        text = index.inc().toString(), shape = CircleShape
                    )
                })
                items(items = buildList {
                    add(StepperItem(title = "Title6", indicator = "a"))
                    add(StepperItem(title = "Title7", indicator = "b"))
                    add(StepperItem(title = "Title8", indicator = "c"))
                    add(StepperItem(title = "Title9", indicator = "d"))
                    add(StepperItem(title = "Title10", indicator = "e"))
                }, itemContent = { item ->
                    Text(text = item.title)
                }, indicatorContent = { item ->
                    StepperIndicator(
                        text = item.indicator, shape = RectangleShape
                    )
                })
            }
        }
    }
}

@Composable
private fun StepperIndicator(text: String, shape: Shape) {
    Box(
        Modifier
            .size(24.dp)
            .clip(shape)
            .background(Color.White, shape)
            .border(1.dp, Color.Black, shape),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text)
    }
}
