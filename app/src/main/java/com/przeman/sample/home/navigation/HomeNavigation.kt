package com.przeman.sample.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.przeman.sample.home.HomeScreen

const val homeRoute = "home_route"

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(route = homeRoute) {
        HomeScreen(navController = navController)
    }
}
