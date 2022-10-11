package com.przeman.retained

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.CoroutineContext

private class ProduceRetainedStateScopeImpl<T>(
    state: MutableState<T>,
    override val coroutineContext: CoroutineContext
) : ProduceStateScope<T>, MutableState<T> by state {

    override suspend fun awaitDispose(onDispose: () -> Unit): Nothing {
        try {
            suspendCancellableCoroutine<Nothing> {}
        } finally {
            onDispose()
        }
    }
}

@Composable
fun <T> produceRetainedState(
    initialValue: T,
    producer: suspend ProduceStateScope<T>.() -> Unit
): State<T> {
    val result = rememberRetained { mutableStateOf(initialValue) }
    LaunchedEffect(Unit) { ProduceRetainedStateScopeImpl(result, coroutineContext).producer() }
    return result
}

@Composable
fun <T> produceRetainedState(
    initialValue: T,
    key1: Any?,
    producer: suspend ProduceStateScope<T>.() -> Unit
): State<T> {
    val result = rememberRetained { mutableStateOf(initialValue) }
    LaunchedEffect(key1) { ProduceRetainedStateScopeImpl(result, coroutineContext).producer() }
    return result
}

@Composable
fun <T> produceRetainedState(
    initialValue: T,
    key1: Any?,
    key2: Any?,
    producer: suspend ProduceStateScope<T>.() -> Unit
): State<T> {
    val result = rememberRetained { mutableStateOf(initialValue) }
    LaunchedEffect(key1, key2) { ProduceRetainedStateScopeImpl(result, coroutineContext).producer() }
    return result
}

@Composable
fun <T> produceRetainedState(
    initialValue: T,
    key1: Any?,
    key2: Any?,
    key3: Any?,
    producer: suspend ProduceStateScope<T>.() -> Unit
): State<T> {
    val result = rememberRetained { mutableStateOf(initialValue) }
    LaunchedEffect(key1, key2, key3) {
        ProduceRetainedStateScopeImpl(result, coroutineContext).producer()
    }
    return result
}

@Composable
fun <T> produceRetainedState(
    initialValue: T,
    vararg keys: Any?,
    producer: suspend ProduceStateScope<T>.() -> Unit
): State<T> {
    val result = rememberRetained { mutableStateOf(initialValue) }
    LaunchedEffect(keys = keys) { ProduceRetainedStateScopeImpl(result, coroutineContext).producer() }
    return result
}
