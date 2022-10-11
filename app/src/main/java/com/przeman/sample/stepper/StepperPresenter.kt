package com.przeman.sample.stepper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.przeman.retained.produceRetainedState
import com.przeman.sample.arch.Presenter
import com.przeman.sample.arch.UiParam
import com.przeman.sample.stepper.data.StepperItemRepository
import kotlinx.coroutines.flow.collectLatest

class StepperPresenter(
    private val repo: StepperItemRepository,
) : Presenter<StepperParam> {

    @Composable
    override fun present(): StepperParam {
        val state =
            produceRetainedState(initialValue = StepperParam(emptyList()), key1 = repo, producer = {
                repo.generate().collectLatest { items ->
                    value = StepperParam(items.map {
                        StepperItem(title = it.text, indicator = it.indicator)
                    })
                }
            })
        return state.value
    }
}

data class StepperParam(val items: List<StepperItem>) : UiParam

@Immutable
data class StepperItem(
    val title: String,
    val indicator: String = "",
)
