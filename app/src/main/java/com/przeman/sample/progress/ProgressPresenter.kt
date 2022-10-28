package com.przeman.sample.progress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.przeman.arch.Presenter
import com.przeman.arch.UiEvent
import com.przeman.arch.UiParam
import com.przeman.retained.rememberRetained
import javax.inject.Inject

class ProgressPresenter @Inject constructor() : Presenter<ProgressParam> {

    @Composable
    override fun present(): ProgressParam {
        val progressValue = rememberRetained {
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
