package com.przeman.retained.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

class Continuity : ViewModel(), RetainedStateRegistry {
    private val delegate = RetainedStateRegistryImpl(null)

    override fun consumeValue(key: String): Any? {
        return delegate.consumeValue(key)
    }

    override fun registerValue(
        key: String,
        valueProvider: () -> Any?
    ): RetainedStateRegistry.Entry {
        return delegate.registerValue(key, valueProvider)
    }

    override fun performSave() {
        delegate.performSave()
    }

    override fun forgetUnclaimedValues() {
        delegate.forgetUnclaimedValues()
    }

    override fun onCleared() {
        delegate.retained.clear()
        delegate.valueProviders.clear()
    }

    internal companion object {
        const val KEY = "Continuity"
    }
}

/**
 * Provides a [RetainedStateRegistry].
 *
 * @param factory an optional [ViewModelProvider.Factory] to use when creating the [Continuity]
 * instance.
 */
@Composable
internal fun continuityRetainedStateRegistry(
    factory: ViewModelProvider.Factory? = null
): RetainedStateRegistry {
    val vm = viewModel<Continuity>(key = Continuity.KEY, factory = factory)
    val canRetain = rememberCanRetainChecker()
    remember(canRetain) {
        object : RememberObserver {
            override fun onAbandoned() = unregisterIfNotRetainable()

            override fun onForgotten() = unregisterIfNotRetainable()

            override fun onRemembered() {
                // Do nothing
            }

            fun unregisterIfNotRetainable() {
                if (canRetain()) {
                    vm.performSave()
                }
            }
        }
    }
    LaunchedEffect(vm) {
        withFrameNanos {}
        // This resumes after the just-composed frame completes drawing. Any unclaimed values at this
        // point can be assumed to be no longer used
        vm.forgetUnclaimedValues()
    }
    return vm
}
