package com.przeman.stepper.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.przeman.shared.SizeM
import com.przeman.shared.SizeS

@Composable
internal fun StepperItem(
    content: @Composable () -> Unit,
    indicator: (@Composable () -> Unit)? = null,
) {
    StepperItemRow {
        Indicator(indicator)
        Content(content)
    }
}

@Composable
private fun StepperItemRow(
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
private fun Indicator(
    indicator: @Composable (() -> Unit)? = null,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(SizeM),
            contentAlignment = Alignment.Center
        ) {
            if (indicator != null) {
                indicator()
            }
        }
        Spacer(modifier = Modifier.width(SizeS))
    }
}

@Composable
private fun RowScope.Content(content: @Composable () -> Unit) {
    Row(
        Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}
