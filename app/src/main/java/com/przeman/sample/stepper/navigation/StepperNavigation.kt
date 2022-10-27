package com.przeman.sample.stepper.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.przeman.sample.navigation.Destination
import com.przeman.sample.stepper.StepperScreen

const val stepperRoute = "stepper_route"

fun NavGraphBuilder.stepperScreen() {
    composable(route = stepperRoute) {
        StepperScreen()
    }
}

data class StepperDestination(override val route: String = stepperRoute) : Destination
