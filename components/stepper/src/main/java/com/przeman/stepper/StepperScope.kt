package com.przeman.stepper

import androidx.compose.runtime.Composable
import com.przeman.stepper.internal.IntervalList
import com.przeman.stepper.internal.MutableIntervalList
import com.przeman.stepper.internal.StepperItem

interface StepperScope {
    fun items(
        count: Int,
        itemContent: @Composable (index: Int) -> Unit,
        indicatorContent: (@Composable (index: Int) -> Unit)? = null,
    )
}

inline fun <T> StepperScope.items(
    items: List<T>,
    crossinline itemContent: @Composable (item: T) -> Unit,
    crossinline indicatorContent: @Composable (item: T) -> Unit,
) = items(
    count = items.size,
    itemContent = { itemContent(items[it]) },
    indicatorContent = { indicatorContent(items[it]) },
)

inline fun <T> StepperScope.itemsIndexed(
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

internal class StepperScopeImpl : StepperScope {

    private val _intervals = MutableIntervalList<StepperContent>()
    val intervals: IntervalList<StepperContent> = _intervals

    override fun items(
        count: Int,
        itemContent: @Composable (index: Int) -> Unit,
        indicatorContent: (@Composable (index: Int) -> Unit)?,
    ) {
        _intervals.add(
            count,
            StepperContent(
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

internal class StepperContent(
    val content: (index: Int) -> @Composable () -> Unit,
)
