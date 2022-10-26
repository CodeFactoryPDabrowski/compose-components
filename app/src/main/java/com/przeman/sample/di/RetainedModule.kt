package com.przeman.sample.di

import androidx.lifecycle.ViewModel
import com.przeman.retained.internal.Continuity
import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@ContributesTo(AppScope::class)
@Module
interface RetainedModule {
    @ViewModelKey(Continuity::class)
    @IntoMap
    @Binds
    fun Continuity.bindContinuity(): ViewModel

    companion object {
        @Provides
        fun provideContinuity(): Continuity {
            return Continuity()
        }
    }
}
