package com.przeman.stepper

import androidx.compose.runtime.Composable

interface StepperColumnScope {
    fun items(
        count: Int,
        itemContent: @Composable (index: Int) -> Unit,
        indicatorContent: (@Composable (index: Int) -> Unit)? = null,
    )
}

inline fun <T> StepperColumnScope.items(
    items: List<T>,
    crossinline itemContent: @Composable (item: T) -> Unit,
    crossinline indicatorContent: @Composable (item: T) -> Unit,
) = items(
    count = items.size,
    itemContent = { itemContent(items[it]) },
    indicatorContent = { indicatorContent(items[it]) },
)

inline fun <T> StepperColumnScope.itemsIndexed(
    items: List<T>,
    crossinline itemContent: @Composable (index: Int, item: T) -> Unit,
    crossinline indicatorContent: @Composable (index: Int, item: T) -> Unit,
) = items(
    count = items.size,
    itemContent = {
        itemContent(it, items[it])
    },
    indicatorContent = {
        indicatorContent(it, items[it])
    },
)

internal class StepperColumnScopeImpl : StepperColumnScope {

    private val _intervals = MutableIntervalList<StepperColumnContent>()
    val intervals: IntervalList<StepperColumnContent> = _intervals

    override fun items(
        count: Int,
        itemContent: @Composable (index: Int) -> Unit,
        indicatorContent: (@Composable (index: Int) -> Unit)?,
    ) {
        _intervals.add(
            count,
            StepperColumnContent(
                content = { index ->
                    @Composable {
                        StepperItem(
                            content = { itemContent(index) },
                            indicator = { indicatorContent?.invoke(index) }
                        )
                    }
                })
        )
    }
}

internal class StepperColumnContent(
    val content: (index: Int) -> @Composable () -> Unit,
)
