package com.przeman.retained

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.przeman.retained.internal.LocalRetainedStateRegistryOwner
import com.przeman.retained.internal.NoOpRetainedStateRegistry
import com.przeman.retained.internal.rememberCanRetainChecker

@Composable
fun <T : Any> rememberRetained(vararg inputs: Any?, key: String? = null, init: () -> T): T {
    val registry = LocalRetainedStateRegistryOwner.current
    // In case RetainedCompositionLocals was not called
    if (registry === NoOpRetainedStateRegistry) {
        return when (inputs.size) {
            0 -> remember(init)
            1 -> remember(inputs[0], init)
            2 -> remember(inputs[0], inputs[1], init)
            3 -> remember(inputs[0], inputs[1], inputs[2], init)
            else -> remember(keys = inputs, init)
        }
    }

    // key is the one provided by the user or the one generated by the compose runtime
    val finalKey =
        if (!key.isNullOrEmpty()) {
            key
        } else {
            currentCompositeKeyHash.toString(MaxSupportedRadix)
        }

    // value is restored using the registry or created via [init] lambda
    val value =
        remember(*inputs) {
            val restored = registry.consumeValue(finalKey)
            restored ?: init()
        }

    // we want to use the latest instances of value in the valueProvider lambda
    // without restarting DisposableEffect as it would cause re-registering the provider in
    // the different order. so we use rememberUpdatedState.
    val valueState = rememberUpdatedState(value)

    val canRetain = rememberCanRetainChecker()
    remember(registry, finalKey) {
        val entry = registry.registerValue(finalKey) { valueState.value }
        object : RememberObserver {
            override fun onAbandoned() = unregisterIfNotRetainable()

            override fun onForgotten() = unregisterIfNotRetainable()

            override fun onRemembered() {
                // Do nothing
            }

            fun unregisterIfNotRetainable() {
                if (!canRetain()) {
                    entry.unregister()
                }
            }
        }
    }
    @Suppress("UNCHECKED_CAST") return value as T
}

/** The maximum radix available for conversion to and from strings. */
private const val MaxSupportedRadix = 36
