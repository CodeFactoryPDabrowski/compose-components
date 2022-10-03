package com.przeman.sample.arch

import androidx.compose.runtime.Composable

interface Presenter<Param : UiParam> {

    @Composable
    fun present(): Param
}
