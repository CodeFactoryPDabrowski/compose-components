package com.przeman.progress.internal

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.debugInspectorInfo
import kotlinx.coroutines.launch

internal fun Modifier.pressModifier(
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
    name = "pressModifier"
    properties["draggableState"] = draggableState
    properties["interactionSource"] = interactionSource
    properties["maxPx"] = maxPx
    properties["isRtl"] = isRtl
    properties["rawOffset"] = rawOffset
    properties["gestureEndAction"] = gestureEndAction
    properties["pressOffset"] = pressOffset
    properties["enabled"] = enabled
})
