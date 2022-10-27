package com.przeman.sample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.przeman.sample.home.navigation.homeRoute
import com.przeman.sample.home.navigation.homeScreen
import com.przeman.sample.progress.navigation.progressScreen
import com.przeman.sample.stepper.navigation.stepperScreen

@Composable
fun ComponentsApp() {
    ComponentsNavHost(navController = rememberNavController())
}

@Composable
fun ComponentsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberNavigator(navController)

    NavHost(
        navController = navController,
        startDestination = homeRoute,
        modifier = modifier,
    ) {
        homeScreen(navigator)
        progressScreen()
        stepperScreen()
    }
}
