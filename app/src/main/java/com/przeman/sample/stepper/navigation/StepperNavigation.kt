package com.przeman.sample.stepper.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.przeman.sample.stepper.StepperScreen

const val stepperRoute = "stepper_route"

fun NavController.navigateToStepper(navOptions: NavOptions? = null) {
    this.navigate(stepperRoute, navOptions)
}

fun NavGraphBuilder.stepperScreen() {
    composable(route = stepperRoute) {
        StepperScreen()
    }
}
