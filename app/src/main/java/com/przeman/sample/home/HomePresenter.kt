package com.przeman.sample.home

import androidx.compose.runtime.Composable
import com.przeman.sample.arch.Navigator
import com.przeman.sample.arch.Presenter
import com.przeman.sample.arch.UiEvent
import com.przeman.sample.arch.UiParam
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator
) : Presenter<HomeParam> {
    @Composable
    override fun present(): HomeParam {
        return HomeParam { event ->
            when (event) {
                is Event.ClickedItem -> navigator.gotTo(event.destination)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}

data class HomeParam(val onSink: (Event) -> Unit) : UiParam

sealed interface Event : UiEvent {
    data class ClickedItem(val destination: Navigator.Destination) : Event
}
