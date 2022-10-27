package com.przeman.sample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.przeman.sample.home.navigation.homeRoute
import com.przeman.sample.home.navigation.homeScreen
import com.przeman.sample.progress.navigation.progressScreen
import com.przeman.sample.stepper.navigation.stepperScreen

@Composable
fun ComponentsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = homeRoute,
        modifier = modifier,
    ) {
        homeScreen(navController)
        progressScreen()
        stepperScreen()
    }
}
