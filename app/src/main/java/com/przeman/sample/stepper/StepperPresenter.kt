package com.przeman.sample.stepper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.przeman.sample.arch.Presenter
import com.przeman.sample.arch.UiParam

class StepperPresenter : Presenter<StepperParam> {

    @Composable
    override fun present(): StepperParam {
        TODO("Not yet implemented")
    }
}

data class StepperParam(val items: List<StepperItem>) : UiParam

@Immutable
data class StepperItem(
    val title: String,
    val indicator: String = "",
)
