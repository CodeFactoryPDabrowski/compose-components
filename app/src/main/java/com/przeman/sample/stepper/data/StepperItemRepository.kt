package com.przeman.sample.stepper.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class StepperItemRepository {

    fun generate(): Flow<List<StepperItemDTO>> {
        val stepperItems = mutableListOf<StepperItemDTO>()
        return (1..20).asSequence().asFlow().onEach { delay(1_000) }.map {
            stepperItems.add(StepperItemDTO("Item $it", "$it"))
            stepperItems
        }
    }
}

data class StepperItemDTO(
    val text: String, val indicator: String
)
