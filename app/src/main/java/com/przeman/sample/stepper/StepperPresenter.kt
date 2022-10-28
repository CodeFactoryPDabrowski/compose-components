package com.przeman.sample.stepper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import com.przeman.retained.produceRetainedState
import com.przeman.arch.Presenter
import com.przeman.arch.UiParam
import com.przeman.sample.stepper.data.StepperItemRepository
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StepperPresenter @Inject constructor(
    private val repo: StepperItemRepository,
) : Presenter<StepperParam> {

    @Composable
    override fun present(): StepperParam {
        val items by produceRetainedState(
            initialValue = listOf(),
            key1 = repo,
            producer = { repo.generate(value).collectLatest { value = it } },
        )
        return StepperParam(items.map {
            StepperItem(title = it.text, indicator = it.indicator)
        })
    }
}

data class StepperParam(val items: List<StepperItem>) : UiParam

@Immutable
data class StepperItem(
    val title: String,
    val indicator: String = "",
)
