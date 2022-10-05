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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.przeman.shared.SizeM

@Composable
fun HomeScreen(navController: NavController) {
    val presenter = remember {
        HomePresenter(onNavigationChange = { navController.navigate(it) })
    }
    val param = presenter.present()

    HomeContent { screen -> param.onSink(Event.ClickedItem(screen)) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(onNavigationChange: (String) -> Unit) {
    Scaffold(
        modifier = Modifier
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
