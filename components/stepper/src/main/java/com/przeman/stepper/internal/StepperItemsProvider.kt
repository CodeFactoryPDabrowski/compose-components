package com.przeman.stepper.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.przeman.stepper.StepperContent
import com.przeman.stepper.StepperScope
import com.przeman.stepper.StepperScopeImpl

internal interface StepperItemsProvider {
    fun getContent(index: Int): @Composable () -> Unit
    val itemsCount: Int
}

@Composable
internal fun rememberStateOfItemsProvider(
    content: StepperScope.() -> Unit,
): State<StepperItemsProvider> {
    val latestContent = rememberUpdatedState(content)
    return remember {
        derivedStateOf<StepperItemsProvider> {
            val stepperScope = StepperScopeImpl().apply(latestContent.value)
            StepperItemsProviderImpl(stepperScope.intervals)
        }
    }
}

internal class StepperItemsProviderImpl(
    private val list: IntervalList<StepperContent>,
) : StepperItemsProvider {

    override fun getContent(index: Int): @Composable () -> Unit {
        val interval = list.intervalForIndex(index)
        val localIntervalIndex = index - interval.startIndex

        return interval.content.content.invoke(localIntervalIndex)
    }

    override val itemsCount get() = list.totalSize
}
