package com.przeman.sample.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.przeman.sample.arch.Navigator

@Composable
fun rememberNavigator(
    navController: NavController,
): Navigator {
    return remember { NavigatorImpl(navController) }
}


private class NavigatorImpl(
    private val navController: NavController
) : Navigator {

    override fun gotTo(destination: Navigator.Destination) {
        navController.navigate(destination.route, null) //TODO: Handle nav options if needed
    }

    override fun goBack() {
        navController.popBackStack()
    }

}
