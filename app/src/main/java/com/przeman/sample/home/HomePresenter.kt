package com.przeman.sample.home

import androidx.compose.runtime.Composable
import com.przeman.sample.arch.Presenter
import com.przeman.sample.arch.UiEvent
import com.przeman.sample.arch.UiParam

//TODO: DI, Navigation! Just for testing
class HomePresenter(private val onNavigationChange: (String) -> Unit) : Presenter<HomeParam> {
    @Composable
    override fun present(): HomeParam {
        return HomeParam { event ->
            when (event) {
                is Event.ClickedItem -> onNavigationChange(event.route)
            }
        }
    }
}

data class HomeParam(val onSink: (Event) -> Unit) : UiParam

sealed interface Event : UiEvent {
    data class ClickedItem(val route: String) : Event
}
