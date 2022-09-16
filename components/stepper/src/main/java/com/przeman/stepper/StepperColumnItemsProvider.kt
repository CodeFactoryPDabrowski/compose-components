package com.przeman.stepper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

internal interface StepperColumnItemsProvider {
    fun getContent(index: Int): @Composable () -> Unit
    val itemsCount: Int
}

@Composable
internal fun rememberStateOfItemsProvider(
    content: StepperColumnScope.() -> Unit,
): State<StepperColumnItemsProvider> {
    val latestContent = rememberUpdatedState(content)
    return remember {
        derivedStateOf<StepperColumnItemsProvider> {
            val stepperColumnScope = StepperColumnScopeImpl().apply(latestContent.value)
            StepperColumnItemsProviderImpl(stepperColumnScope.intervals)
        }
    }
}

internal class StepperColumnItemsProviderImpl(
    private val list: IntervalList<StepperColumnContent>,
) : StepperColumnItemsProvider {

    override fun getContent(index: Int): @Composable () -> Unit {
        val interval = list.intervalForIndex(index)
        val localIntervalIndex = index - interval.startIndex

        return interval.content.content.invoke(localIntervalIndex)
    }

    override val itemsCount get() = list.totalSize
}
