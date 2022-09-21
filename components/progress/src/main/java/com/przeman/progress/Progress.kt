package com.przeman.progress

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.przeman.shared.PreviewBox
import com.przeman.shared.SizeM
import com.przeman.shared.SizeS
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

@Composable
fun Progress(
    modifier: Modifier = Modifier,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    color: Color = Color.DarkGray,
    trackColor: Color = color.copy(alpha = 0.25f),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onValueChange: (Float) -> Unit = {},
    onValueChangeFinished: (() -> Unit)? = null,
    steps: Int = 0,
) {
    val normalizedValue = value.coerceIn(valueRange.start, valueRange.endInclusive)
    val animationProgress by rememberInfiniteTransition().animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(1_000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        )
    )

    val onValueChangeState = rememberUpdatedState(onValueChange)
    val tickFractions = remember(steps) {
        stepsToTickFractions(steps)
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        val widthPx = constraints.maxWidth.toFloat()
        val maxPx: Float
        val minPx: Float
        with(LocalDensity.current) {
            maxPx = max(widthPx - ProgressDefaults.thumbRadius.toPx(), 0f)
            minPx = min(ProgressDefaults.thumbRadius.toPx(), maxPx)
        }

        fun scaleToUserValue(offset: Float) =
            scale(minPx, maxPx, offset, valueRange.start, valueRange.endInclusive)

        fun scaleToOffset(userValue: Float) =
            scale(valueRange.start, valueRange.endInclusive, userValue, minPx, maxPx)

        val scope = rememberCoroutineScope()
        val rawOffset = remember { mutableStateOf(scaleToOffset(value)) }
        val pressOffset = remember { mutableStateOf(0f) }

        val draggableState = remember(minPx, maxPx, valueRange) {
            SliderDraggableState {
                rawOffset.value = (rawOffset.value + it + pressOffset.value)
                pressOffset.value = 0f
                val offsetInTrack = rawOffset.value.coerceIn(minPx, maxPx)
                onValueChangeState.value.invoke(scaleToUserValue(offsetInTrack))
            }
        }

        CorrectValueSideEffect(::scaleToOffset, valueRange, minPx..maxPx, rawOffset, value)

        val gestureEndAction = rememberUpdatedState<(Float) -> Unit> { velocity: Float ->
            val current = rawOffset.value
            val target =
                snapValueToTick(current, tickFractions, minPx, maxPx)
            if (current != target) {
                scope.launch {
                    animateToTarget(
                        draggableState,
                        current,
                        target,
                        velocity
                    )
                    onValueChangeFinished?.invoke()
                }
            } else if (!draggableState.isDragging) {
                // check ifDragging in case the change is still in progress (touch -> drag case)
                onValueChangeFinished?.invoke()
            }
        }

        val press = Modifier.sliderTapModifier(
            draggableState,
            interactionSource,
            widthPx,
            isRtl,
            rawOffset,
            gestureEndAction,
            pressOffset,
            enabled
        )

        val drag = Modifier.draggable(
            orientation = Orientation.Horizontal,
            reverseDirection = isRtl,
            enabled = enabled,
            interactionSource = interactionSource,
            onDragStopped = { velocity -> gestureEndAction.value.invoke(velocity) },
            startDragImmediately = draggableState.isDragging,
            state = draggableState
        )

        val fraction = calcFraction(
            valueRange.start,
            valueRange.endInclusive,
            normalizedValue,
        )

        ProgressImpl(
            modifier = press.then(drag),
            fraction = fraction,
            color = color,
            trackColor = trackColor,
            animationProgress = animationProgress,
            width = maxPx - minPx,
            interactionSource = interactionSource,
        )
    }
}

@Composable
private fun ProgressImpl(
    modifier: Modifier,
    fraction: Float,
    color: Color,
    trackColor: Color,
    animationProgress: Float,
    width: Float,
    interactionSource: MutableInteractionSource,
) {
    Box(modifier) {
        val widthDp: Dp
        with(LocalDensity.current) {
            widthDp = width.toDp()
        }

        val offset = widthDp * fraction
        Track(
            fraction = fraction,
            color = color,
            animationProgress = animationProgress,
            trackColor = trackColor,
        )
        Thumb(offset = offset, color = color, interactionSource = interactionSource)
    }
}

@Composable
private fun BoxScope.Track(
    fraction: Float,
    color: Color,
    animationProgress: Float,
    trackColor: Color,
) {
    Canvas(
        Modifier
            .progressSemantics(fraction)
            .fillMaxWidth()
            .align(Alignment.Center)
    ) {
        drawSineIndicator(
            startFraction = 0f,
            endFraction = fraction,
            color = color,
            animationProgress = animationProgress,
        )
        drawLinearIndicator(
            startFraction = fraction,
            endFraction = 1f,
            color = trackColor,
            strokeWidth = ProgressDefaults.progressThicknessDp.toPx()
        )
    }
}

@Composable
private fun BoxScope.Thumb(
    offset: Dp,
    color: Color,
    interactionSource: MutableInteractionSource,
) {
    Box(
        Modifier
            .padding(start = offset)
            .align(Alignment.CenterStart)
    ) {
        val interactions = remember { mutableStateListOf<Interaction>() }
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> interactions.add(interaction)
                    is PressInteraction.Release -> interactions.remove(interaction.press)
                    is PressInteraction.Cancel -> interactions.remove(interaction.press)
                    is DragInteraction.Start -> interactions.add(interaction)
                    is DragInteraction.Stop -> interactions.remove(interaction.start)
                    is DragInteraction.Cancel -> interactions.remove(interaction.start)
                }
            }
        }

        Spacer(
            Modifier
                .size(ProgressDefaults.thumbRadius.times(2))
                .hoverable(interactionSource = interactionSource)
                .background(color, CircleShape)
        )
    }
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
    val thumbRadius = 10.dp
}

// Scale x1 from a1..b1 range to a2..b2 range
private fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
    lerp(a2, b2, calcFraction(a1, b1, x1))

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

private class SliderDraggableState(
    val onDelta: (Float) -> Unit
) : DraggableState {

    var isDragging by mutableStateOf(false)
        private set

    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = onDelta(pixels)
    }

    private val scrollMutex = MutatorMutex()

    override suspend fun drag(
        dragPriority: MutatePriority, block: suspend DragScope.() -> Unit
    ): Unit = coroutineScope {
        isDragging = true
        scrollMutex.mutateWith(dragScope, dragPriority, block)
        isDragging = false
    }

    override fun dispatchRawDelta(delta: Float) {
        return onDelta(delta)
    }
}

private fun snapValueToTick(
    current: Float,
    tickFractions: List<Float>,
    minPx: Float,
    maxPx: Float
): Float {
    // target is a closest anchor to the `current`, if exists
    return tickFractions
        .minByOrNull { abs(lerp(minPx, maxPx, it) - current) }
        ?.run { lerp(minPx, maxPx, this) }
        ?: current
}

@Composable
private fun CorrectValueSideEffect(
    scaleToOffset: (Float) -> Float,
    valueRange: ClosedFloatingPointRange<Float>,
    trackRange: ClosedFloatingPointRange<Float>,
    valueState: MutableState<Float>,
    value: Float
) {
    SideEffect {
        val error = (valueRange.endInclusive - valueRange.start) / 1000
        val newOffset = scaleToOffset(value)
        if (abs(newOffset - valueState.value) > error) {
            if (valueState.value in trackRange) {
                valueState.value = newOffset
            }
        }
    }
}

private fun Modifier.sliderTapModifier(
    draggableState: DraggableState,
    interactionSource: MutableInteractionSource,
    maxPx: Float,
    isRtl: Boolean,
    rawOffset: State<Float>,
    gestureEndAction: State<(Float) -> Unit>,
    pressOffset: MutableState<Float>,
    enabled: Boolean
) = composed(factory = {
    if (enabled) {
        val scope = rememberCoroutineScope()
        pointerInput(draggableState, interactionSource, maxPx, isRtl) {
            detectTapGestures(onPress = { pos ->
                val to = if (isRtl) maxPx - pos.x else pos.x
                pressOffset.value = to - rawOffset.value
                try {
                    awaitRelease()
                } catch (_: GestureCancellationException) {
                    pressOffset.value = 0f
                }
            }, onTap = {
                scope.launch {
                    draggableState.drag(MutatePriority.UserInput) {
                        // just trigger animation, press offset will be applied
                        dragBy(0f)
                    }
                    gestureEndAction.value.invoke(0f)
                }
            })
        }
    } else {
        this
    }
}, inspectorInfo = debugInspectorInfo {
    name = "sliderTapModifier"
    properties["draggableState"] = draggableState
    properties["interactionSource"] = interactionSource
    properties["maxPx"] = maxPx
    properties["isRtl"] = isRtl
    properties["rawOffset"] = rawOffset
    properties["gestureEndAction"] = gestureEndAction
    properties["pressOffset"] = pressOffset
    properties["enabled"] = enabled
})

private suspend fun animateToTarget(
    draggableState: DraggableState, current: Float, target: Float, velocity: Float
) {
    draggableState.drag {
        var latestValue = current
        Animatable(initialValue = current).animateTo(target, SliderToTickAnimation, velocity) {
            dragBy(this.value - latestValue)
            latestValue = this.value
        }
    }
}

private fun stepsToTickFractions(steps: Int): List<Float> {
    return if (steps == 0) emptyList() else List(steps + 2) { it.toFloat() / (steps + 1) }
}

private val SliderToTickAnimation = TweenSpec<Float>(durationMillis = 100)

@Preview
@Composable
private fun ProgressPreview() {
    PreviewBox {
        Column(
            modifier = Modifier.padding(SizeS), verticalArrangement = Arrangement.spacedBy(SizeM)
        ) {
            Progress(value = 0.5f)
            Progress(value = 0.9f)
            Progress(value = 0.2f)
        }
    }
}
