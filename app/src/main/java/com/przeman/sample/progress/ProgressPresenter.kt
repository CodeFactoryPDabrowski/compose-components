package com.przeman.sample.progress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.przeman.sample.arch.Presenter
import com.przeman.sample.arch.UiEvent
import com.przeman.sample.arch.UiParam

class ProgressPresenter : Presenter<ProgressParam> {

    @Composable
    override fun present(): ProgressParam {
        val progressValue = remember {
            mutableStateOf(0.5f)
        }
        return ProgressParam(progressValue.value) { event ->
            when (event) {
                is Event.ProgressChange -> progressValue.value = event.change
            }
        }
    }
}

data class ProgressParam(
    val progress: Float,
    val onSink: (Event) -> Unit,
) : UiParam

sealed interface Event : UiEvent {
    data class ProgressChange(val change: Float) : Event
}
