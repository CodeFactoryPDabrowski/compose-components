package com.przeman.stepper

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import com.przeman.shared.SizeS
import com.przeman.shared.SizeXS

@Composable
fun StepperColumn(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = SizeS,
    lineColor: Color,
    lineEffect: PathEffect?,
    content: StepperColumnScope.() -> Unit,
) {
    val stateOfItemsProvider = rememberStateOfItemsProvider(content = content)
    val measurePolicy =
        rememberStepperColumnMeasurePolicy(
            stateOfItemsProvider = stateOfItemsProvider,
            itemSpacing = itemSpacing,
            line = { height ->
                VerticalLine(
                    height = height,
                    color = lineColor,
                    lineEffect = lineEffect
                )
            },
        )

    SubcomposeLayout(modifier = modifier, measurePolicy = measurePolicy)
}

@Composable
private fun rememberStepperColumnMeasurePolicy(
    stateOfItemsProvider: State<StepperColumnItemsProvider>,
    itemSpacing: Dp,
    line: @Composable (Int) -> Unit,
): SubcomposeMeasureScope.(Constraints) -> MeasureResult {
    return remember(itemSpacing) {
        { constraints ->
            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
            val itemsContent = @Composable {
                for (i in 0 until stateOfItemsProvider.value.itemsCount) {
                    stateOfItemsProvider.value.getContent(i).invoke()
                }
            }
            val itemsPlaceables =
                subcompose(StepperColumnSlots.Items, itemsContent).fastMap {
                    it.measure(looseConstraints)
                }

            val maxSize =
                itemsPlaceables.foldIndexed(IntSize.Zero) { index, currentMax, placeable ->
                    val itemSpacingHeight = if (index != itemsPlaceables.lastIndex) {
                        itemSpacing.roundToPx()
                    } else {
                        0
                    }
                    IntSize(
                        width = maxOf(currentMax.width, placeable.width),
                        height = currentMax.height + placeable.height + itemSpacingHeight
                    )
                }

            var lineRange: LineRange? = null
            if (itemsPlaceables.size >= 2) {
                lineRange = LineRange(
                    topPaddingPx = itemsPlaceables.first().height / 2,
                    bottomPaddingPx = itemsPlaceables.last().height / 2
                )
            }

            layout(maxSize.width, maxSize.height) {
                if (lineRange != null) {
                    subcompose(StepperColumnSlots.Line) {
                        line(maxSize.height - lineRange.verticalPaddingPx)
                    }.forEach {
                        it.measure(constraints).placeRelative(
                            x = SizeXS.roundToPx(), //Because StepperItem indicator has SizeM
                            y = lineRange.topPaddingPx
                        )
                    }
                }

                var yPosition = 0
                itemsPlaceables.forEachIndexed { index, placeable ->
                    placeable.placeRelative(0, yPosition)
                    yPosition += placeable.height
                    if (index != itemsPlaceables.lastIndex) {
                        yPosition += itemSpacing.roundToPx()
                    }
                }
            }
        }
    }
}

@Composable
private fun VerticalLine(
    height: Int,
    thickness: Dp = 1.dp,
    color: Color,
    lineEffect: PathEffect?,
) {
    val heightDp = with(LocalDensity.current) { height.toDp() }
    Canvas(Modifier.height(heightDp)) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            pathEffect = lineEffect,
            strokeWidth = thickness.toPx()
        )
    }
}

private class LineRange(val topPaddingPx: Int, val bottomPaddingPx: Int) {
    val verticalPaddingPx = topPaddingPx + bottomPaddingPx
}

private enum class StepperColumnSlots { Line, Items }
