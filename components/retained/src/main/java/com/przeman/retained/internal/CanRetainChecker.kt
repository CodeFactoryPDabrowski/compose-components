package com.przeman.retained.internal

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
internal fun rememberCanRetainChecker(): () -> Boolean {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    return remember { { activity?.isChangingConfigurations == true } }
}

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
