package com.przeman.sample.arch

interface Navigator {

    fun gotTo(destination: Destination)

    fun goBack()

    interface Destination {
        val route: String
    }
}
