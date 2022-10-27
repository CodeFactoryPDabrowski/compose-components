package com.przeman.sample.progress.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.przeman.sample.progress.ProgressScreen

const val progressRoute = "progress_route"

fun NavController.navigateToProgress(navOptions: NavOptions? = null) {
    this.navigate(progressRoute, navOptions)
}

fun NavGraphBuilder.progressScreen() {
    composable(route = progressRoute) {
        ProgressScreen()
    }
}
