package com.przeman.sample.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Provider

/** A factory that will provide [ViewModels][ViewModel] using their Dagger provider. */
@ContributesBinding(AppScope::class)
class ComponentsViewModelProviderFactory @Inject constructor(
    private val modelProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val modelProvider = modelProviders[modelClass]
            ?: throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        @Suppress("UNCHECKED_CAST") return modelProvider.get() as T
    }
}
