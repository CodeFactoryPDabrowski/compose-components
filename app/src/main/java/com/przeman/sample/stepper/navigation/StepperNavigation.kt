package com.przeman.sample.stepper.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.przeman.arch.Navigator
import com.przeman.sample.stepper.DaggerStepperComponent
import com.przeman.sample.stepper.StepperScreen

const val stepperRoute = "stepper_route"

fun NavGraphBuilder.stepperScreen() {
    composable(route = stepperRoute) {
        val presenter = DaggerStepperComponent.builder().build().presenter()

        StepperScreen(presenter)
    }
}

data class StepperDestination(override val route: String = stepperRoute) : Navigator.Destination
