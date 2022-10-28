package com.przeman.sample.progress.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.przeman.arch.Navigator
import com.przeman.sample.progress.ProgressScreen

const val progressRoute = "progress_route"

fun NavGraphBuilder.progressScreen() {
    composable(route = progressRoute) {
        ProgressScreen()
    }
}

data class ProgressDestination(override val route: String = progressRoute) : Navigator.Destination
