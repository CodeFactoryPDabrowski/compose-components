package com.przeman.sample.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.przeman.sample.arch.Navigator
import com.przeman.sample.progress.navigation.ProgressDestination
import com.przeman.sample.stepper.navigation.StepperDestination
import com.przeman.shared.SizeM

@Composable
fun HomeScreen(presenter: HomePresenter) {
    val param = presenter.present()

    HomeContent { screen -> param.onSink(Event.ClickedItem(screen)) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(onNavigationChange: (Navigator.Destination) -> Unit) {
    Scaffold(modifier = Modifier
        .navigationBarsPadding()
        .systemBarsPadding()
        .fillMaxWidth(),
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(modifier = Modifier.padding(horizontal = SizeM), text = "Compose components")
            })
        }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = SizeM)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(SizeM),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(SizeM))

            TextButton(text = "Stepper") {
                onNavigationChange(StepperDestination())
            }
            TextButton(text = "Progress") {
                onNavigationChange(ProgressDestination())
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
