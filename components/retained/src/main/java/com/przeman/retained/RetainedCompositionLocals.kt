package com.przeman.retained

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.przeman.retained.internal.LocalRetainedStateRegistryOwner
import com.przeman.retained.internal.continuityRetainedStateRegistry

@Composable
fun RetainedCompositionLocals(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalRetainedStateRegistryOwner provides continuityRetainedStateRegistry(),
    ) {
        content()
    }
}
