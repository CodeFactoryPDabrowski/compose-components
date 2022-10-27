package com.przeman.sample.home

import com.przeman.sample.di.AppScope
import com.squareup.anvil.annotations.MergeComponent
import dagger.Component

@MergeComponent(scope = AppScope::class) //TODO: Use codegen to create component with presenter!
interface HomeComponent {

    @Component.Builder
    interface Builder {
        fun build(): HomeComponent
    }

    fun presenterFactory(): HomePresenter.Factory
}
