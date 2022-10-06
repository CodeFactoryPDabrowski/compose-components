package com.przeman.sample.stepper

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.przeman.sample.stepper.data.StepperItemRepository
import com.przeman.shared.SizeM
import com.przeman.stepper.Stepper
import com.przeman.stepper.items

@Composable
fun StepperScreen() {
    val presenter = remember { StepperPresenter(StepperItemRepository()) }
    val param = presenter.present()

    StepperContent(param)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepperContent(param: StepperParam) {
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
        ) {
            Stepper(
                modifier = Modifier
                    .padding(horizontal = SizeM)
                    .verticalScroll(rememberScrollState())
            ) {
                items(items = param.items,
                    itemContent = { item -> Text(text = item.title) },
                    indicatorContent = { item ->
                        StepperIndicator(
                            text = item.indicator
                        )
                    })
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
