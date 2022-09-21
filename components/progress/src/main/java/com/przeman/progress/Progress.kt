package com.przeman.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.przeman.shared.PreviewBox
import com.przeman.shared.SizeM
import com.przeman.shared.SizeS
import kotlin.math.ceil
import kotlin.math.sin

@Composable
fun Progress(
    modifier: Modifier = Modifier,
    progress: Float,
    color: Color = Color.DarkGray,
    trackColor: Color = color.copy(alpha = 0.25f),
) {
    val normalizedProgress = progress.coerceIn(0f, 1f)
    val animationProgress by rememberInfiniteTransition().animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(1_000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        )
    )
    Canvas(
        modifier
            .progressSemantics(normalizedProgress)
            .fillMaxWidth()
    ) {
        drawSineIndicator(
            startFraction = 0f,
            endFraction = normalizedProgress,
            color = color,
            animationProgress = animationProgress,
        )
        drawLinearIndicator(
            startFraction = normalizedProgress,
            endFraction = 1f,
            color = trackColor,
            strokeWidth = ProgressDefaults.progressThicknessDp.toPx()
        )
        drawThumb(
            startFraction = normalizedProgress,
            endFraction = 1f,
            color = color,
        )
    }
}

fun DrawScope.drawThumb(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    radius: Dp = ProgressDefaults.progressThicknessDp.times(3)
) {
    val width = size.width
    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else endFraction - startFraction) * width

    drawCircle(
        color = color, radius = radius.toPx(), center = Offset(barStart, 0f),
    )
}

private fun DrawScope.drawLinearIndicator(
    startFraction: Float, endFraction: Float, color: Color, strokeWidth: Float,
) {
    val width = size.width
    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 0f) * width
    val barEnd = (if (isLtr) endFraction - startFraction else startFraction) * width
    val cornerRadius = strokeWidth / 2

    drawRoundRect(
        color = color,
        topLeft = Offset(barStart, 0f),
        size = Size(barEnd, strokeWidth),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
    )
}

private fun DrawScope.drawSineIndicator(
    startFraction: Float, endFraction: Float, color: Color, animationProgress: Float,
) {
    val pathStyle = Stroke(
        width = ProgressDefaults.progressThicknessDp.toPx(),
        join = StrokeJoin.Round,
        cap = StrokeCap.Round,
        pathEffect = PathEffect.cornerPathEffect(radius = ProgressDefaults.wavelengthDp.toPx()),
    )
    val isLtr = layoutDirection == LayoutDirection.Ltr
    drawPath(
        style = pathStyle,
        path = buildSinePath(
            startFraction = startFraction,
            endFraction = endFraction,
            waveOffset = (2 * Math.PI * (if (isLtr) animationProgress else animationProgress.unaryMinus())).toFloat()

        ),
        color = color,
    )
}

private fun DrawScope.buildSinePath(
    startFraction: Float, endFraction: Float, waveOffset: Float = 0f,
): Path {
    val width = size.width
    val height = size.height
    val middleHeight = height.div(2)

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    val wavelength = ProgressDefaults.wavelengthDp.toPx()
    val amplitude = ProgressDefaults.amplitudeDp.toPx()

    val segmentWidth = wavelength / 10
    val numOfPoints = ceil(barEnd.minus(barStart).div(segmentWidth)).toInt() + 1

    return Path().apply {
        var pointX = barStart
        for (point in 0..numOfPoints) {
            val proportionOfWavelength = (pointX - barStart) / wavelength
            val radiansX = proportionOfWavelength * 2 * Math.PI
            val offsetY = (middleHeight + (sin(radiansX + waveOffset) * amplitude)).toFloat()

            when (point) {
                0 -> moveTo(pointX, offsetY)
                else -> lineTo(pointX, offsetY)
            }
            pointX += segmentWidth
        }
    }
}

private object ProgressDefaults {
    val wavelengthDp = 20.dp
    val amplitudeDp = 2.dp
    val progressThicknessDp = 3.dp
}

@Preview
@Composable
private fun ProgressPreview() {
    PreviewBox {
        Column(
            modifier = Modifier.padding(SizeS), verticalArrangement = Arrangement.spacedBy(SizeM)
        ) {
            Progress(progress = 0.5f)
            Progress(progress = 0.9f)
            Progress(progress = 0.2f)
        }
    }
}
