package com.przeman.stepper.internal

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.przeman.shared.PreviewBox
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

@Preview
@Composable
private fun StepperItemIndicatorPreview() {
    PreviewBox {
        StepperItem(
            indicator = {
                Box(
                    Modifier
                        .size(SizeM)
                        .border(1.dp, Color.Black),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "1")
                }
            },
            content = { PreviewContent() }
        )
    }
}

@Preview
@Composable
private fun StepperItemImplPreview() {
    PreviewBox {
        StepperItem(content = { PreviewContent() })
    }
}

@Composable
private fun PreviewContent() {
    Column {
        Text(text = "Title")
        Text(text = "Subtitle")
    }
}
