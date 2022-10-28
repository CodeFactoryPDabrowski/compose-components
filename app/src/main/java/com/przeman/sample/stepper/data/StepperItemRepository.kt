package com.przeman.sample.stepper.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class StepperItemRepository @Inject constructor() {

    fun generate(initial: List<StepperItemDTO>): Flow<List<StepperItemDTO>> {
        val update = initial.toMutableList()
        return (initial.size..20).asSequence().asFlow().onEach { delay(1_000) }.map {
            update.add(StepperItemDTO("Item $it", "$it"))
            update.toList()// Mutable list doesn't call recomposition because it's still the same list -> new immutable list is the solution for this issue
        }
    }
}

data class StepperItemDTO(
    val text: String, val indicator: String
)
