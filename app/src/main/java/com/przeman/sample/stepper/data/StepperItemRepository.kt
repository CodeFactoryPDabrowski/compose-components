package com.przeman.sample.stepper.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class StepperItemRepository {

    fun generate(initial: List<StepperItemDTO>): Flow<List<StepperItemDTO>> {
        val update = initial.toMutableList()
        return (initial.size..20).asSequence().asFlow().onEach { delay(1_000) }.map {
            update.add(StepperItemDTO("Item $it", "$it"))
            update.toList()
        }
    }
}

data class StepperItemDTO(
    val text: String, val indicator: String
)
