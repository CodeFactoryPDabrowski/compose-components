package com.przeman.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.przeman.shared.PreviewBox
import com.przeman.shared.SizeM
import com.przeman.shared.SizeS
import com.przeman.shared.SizeXXXS

@Composable
fun Progress(
    modifier: Modifier = Modifier,
    progress: Float,
    height: Dp = SizeXXXS,
    color: Color = Color.Green,
    trackColor: Color = Color.LightGray,
) {
    val normalizedProgress = progress.coerceIn(0f, 1f)

    Canvas(
        modifier
            .progressSemantics(normalizedProgress)
            .fillMaxWidth()
            .height(height)
    ) {
        val strokeWidth = size.height
        drawLinearIndicator(0f, 1f, trackColor, strokeWidth)
        drawLinearIndicator(0f, normalizedProgress, color, strokeWidth)
    }
}

private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float
) {
    val width = size.width
    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width
    val cornerRadius = strokeWidth / 2

    drawRoundRect(
        color = color,
        topLeft = Offset(barStart, 0f),
        size = Size(barEnd, strokeWidth),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
    )
}

@Preview
@Composable
private fun ProgressPreview() {
    PreviewBox {
        Column(
            modifier = Modifier.padding(SizeS),
            verticalArrangement = Arrangement.spacedBy(SizeM)
        ) {
            Progress(progress = 0f)
            Progress(progress = 0.45f)
            Progress(progress = 1f)
        }
    }
}
