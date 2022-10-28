package com.przeman.sample.progress

import com.przeman.sample.di.AppScope
import com.squareup.anvil.annotations.MergeComponent
import dagger.Component

@MergeComponent(scope = AppScope::class)
interface ProgressComponent {

    @Component.Builder
    interface Builder {
        fun build(): ProgressComponent
    }

    fun presenter(): ProgressPresenter
}
