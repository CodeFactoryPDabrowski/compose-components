package com.przeman.sample.di

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.multibindings.Multibinds

@ContributesTo(AppScope::class)
@Module
interface BaseUiModule {
    @Multibinds
    fun provideViewModelProviders(): Map<Class<out ViewModel>, ViewModel>

    @Multibinds
    fun provideActivityProviders(): Map<Class<out Activity>, Activity>
}
