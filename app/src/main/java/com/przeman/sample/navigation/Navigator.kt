package com.przeman.sample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController

@Composable
fun rememberNavigator(
    navController: NavController,
): Navigator {
    return remember { NavigatorImpl(navController) }
}

interface Navigator {

    fun gotTo(destination: Destination)

    fun goBack()
}

private class NavigatorImpl(
    private val navController: NavController
) : Navigator {

    override fun gotTo(destination: Destination) {
        navController.navigate(destination.route, null) //TODO: Handle nav options if needed
    }

    override fun goBack() {
        navController.popBackStack()
    }

}

interface Destination {
    val route: String
}
