package com.przeman.sample.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.przeman.sample.arch.Navigator
import com.przeman.sample.home.DaggerHomeComponent
import com.przeman.sample.home.HomeScreen

const val homeRoute = "home_route"

fun NavGraphBuilder.homeScreen(navigator: Navigator) {
    composable(route = homeRoute) {
        val presenterFactory = DaggerHomeComponent.builder().build().presenterFactory()
        val presenter = presenterFactory.create(navigator)

        HomeScreen(presenter)
    }
}
