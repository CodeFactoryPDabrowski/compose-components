package com.przeman.retained.internal

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

internal class Continuity : ViewModel(), RetainedStateRegistry {
    private val delegate = RetainedStateRegistryImpl()

    override fun consumeValue(key: String): Any? {
        return delegate.consumeValue(key)
    }

    override fun registerValue(key: String, value: Any?): RetainedStateRegistry.Entry {
        return delegate.registerValue(key, value)
    }

    override fun onCleared() {
        delegate.retained.clear()
    }
}

@Composable
internal fun continuityRetainedStateRegistry(): RetainedStateRegistry {
    return viewModel<Continuity>()
}
