package com.przeman.sample.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.przeman.sample.home.HomeScreen
import com.przeman.sample.navigation.Navigator

const val homeRoute = "home_route"

fun NavGraphBuilder.homeScreen(navigator: Navigator) {
    composable(route = homeRoute) {
        HomeScreen(navigator)
    }
}
