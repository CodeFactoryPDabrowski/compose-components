package com.przeman.stepper

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import com.przeman.shared.PreviewBox
import com.przeman.shared.SizeM
import com.przeman.shared.SizeS
import com.przeman.shared.SizeXXXXXS
import com.przeman.stepper.internal.StepperItemsProvider
import com.przeman.stepper.internal.rememberStateOfItemsProvider

@Composable
fun Stepper(
    modifier: Modifier = Modifier,
    indicatorSize: Dp = SizeM,
    itemSpacing: Dp = SizeS,
    lineColor: Color = Color.Black,
    lineEffect: PathEffect? = null,
    content: StepperScope.() -> Unit,
) {
    val stateOfItemsProvider = rememberStateOfItemsProvider(
        indicatorSize = indicatorSize, content = content
    )
    val measurePolicy = rememberStepperMeasurePolicy(
        stateOfItemsProvider = stateOfItemsProvider,
        indicatorSize = indicatorSize,
        itemSpacing = itemSpacing,
        line = { height ->
            VerticalLine(
                height = height, color = lineColor, lineEffect = lineEffect
            )
        },
    )

    SubcomposeLayout(modifier = modifier, measurePolicy = measurePolicy)
}

@Composable
private fun rememberStepperMeasurePolicy(
    stateOfItemsProvider: State<StepperItemsProvider>,
    indicatorSize: Dp,
    itemSpacing: Dp,
    line: @Composable (Int) -> Unit,
): SubcomposeMeasureScope.(Constraints) -> MeasureResult {
    return remember(indicatorSize, itemSpacing) {
        { constraints ->
            val indicatorMiddle = indicatorSize.div(2)
            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
            val itemsContent = @Composable {
                for (i in 0 until stateOfItemsProvider.value.itemsCount) {
                    stateOfItemsProvider.value.getContent(i).invoke()
                }
            }
            val itemsPlaceables = subcompose(StepperSlots.Items, itemsContent).fastMap {
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
                    subcompose(StepperSlots.Line) {
                        line(maxSize.height - lineRange.verticalPaddingPx)
                    }.forEach {
                        it.measure(constraints).placeRelative(
                            x = indicatorMiddle.roundToPx(), y = lineRange.topPaddingPx
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

private enum class StepperSlots { Line, Items }

@Preview
@Composable
private fun StepperItemsPreview() {
    PreviewBox {
        Stepper {
            items(items = buildList {
                add(StepperItem(title = "Title6", indicator = "a"))
                add(StepperItem(title = "Title7", indicator = "b"))
                add(StepperItem(title = "Title8", indicator = "c"))
                add(StepperItem(title = "Title9", indicator = "d"))
                add(StepperItem(title = "Title10", indicator = "e"))
            }, itemContent = { item ->
                Text(text = item.title)
            }, indicatorContent = { item ->
                StepperIndicator(
                    text = item.indicator
                )
            })
        }
    }
}

@Composable
private fun StepperIndicator(text: String) {
    Box(
        Modifier
            .size(SizeM)
            .clip(CircleShape)
            .background(Color.White, CircleShape)
            .border(SizeXXXXXS, Color.Black, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text)
    }
}

data class StepperItem(val title: String, val indicator: String)
