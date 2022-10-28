package com.przeman.sample.stepper

import com.przeman.sample.di.AppScope
import com.squareup.anvil.annotations.MergeComponent
import dagger.Component

@MergeComponent(scope = AppScope::class)
interface StepperComponent {

    @Component.Builder
    interface Builder {
        fun build(): StepperComponent
    }

    fun presenter(): StepperPresenter

}
