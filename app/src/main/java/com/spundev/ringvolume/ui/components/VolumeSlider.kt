package com.spundev.ringvolume.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spundev.ringvolume.R
import com.spundev.ringvolume.ui.theme.OnSliderColor
import com.spundev.ringvolume.ui.theme.SliderColor

/**
 * Variation of [VolumeSlider] that adds steps between 0 and [maxValue]
 * This will reduce the number of times onVolumeChange is called while keeping a continue drag
 * response.
 */
@Composable
fun StepVolumeSlider(
    value: Int,
    maxValue: Int,
    onVolumeChange: (volume: Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    fillColor: Color = SliderColor,
    iconTintColor: Color = OnSliderColor,
) {
    var lastChangeNotified by remember(value) { mutableIntStateOf(value) }
    var progress by remember { mutableFloatStateOf(0F) }

    LaunchedEffect(value, maxValue) {
        // Only update the slider progress if the new [value] will cause a change to a different
        // step. This will avoid progress jumps when onVolumeChange is triggered and the parent
        // updates [value] with the new volume
        val valueFromProgress = (progress * maxValue).toInt()
        if (valueFromProgress != value) {
            progress = value.toFloat() / maxValue
        }
    }

    VolumeSlider(
        progress = progress,
        onProgressChange = {
            progress = it
            val newVolume = (it * maxValue).toInt()
            if (newVolume != lastChangeNotified) {
                lastChangeNotified = newVolume
                onVolumeChange(newVolume)
            }
        },
        containerColor = containerColor,
        fillColor = fillColor,
        iconRes = if (progress > 0) R.drawable.ic_bell_24 else R.drawable.ic_bell_off_24,
        iconTintColor = iconTintColor,
        modifier = modifier
    )
}

/**
 * @param progress the progress of this slider indicator, where 0.0 represents no progress and 1.0
 * @param onProgressChange - callback in which progress should be updated
 * @param modifier - the Modifier to be applied to this slider
 */
@Composable
private fun VolumeSlider(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    containerColor: Color,
    fillColor: Color,
    @DrawableRes iconRes: Int,
    iconTintColor: Color,
    modifier: Modifier = Modifier
) {
    val vector = ImageVector.vectorResource(id = iconRes)
    val ringBellIcon = rememberVectorPainter(image = vector)
    val ringBellColor = remember(iconTintColor) { ColorFilter.tint(iconTintColor) }

    Canvas(
        modifier = modifier
            .progressSemantics(progress)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newProgress = ((size.height - offset.y) / size.height)
                    onProgressChange(newProgress)
                }
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, _ ->
                    val newProgress = ((size.height - change.position.y) / size.height)
                    onProgressChange(newProgress)
                }
            }
    ) {
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(offset = Offset(0f, 0f), size = size),
                    cornerRadius = CornerRadius(18.dp.toPx()),
                )
            )
        }

        clipPath(
            path = path,
            clipOp = ClipOp.Intersect
        ) {
            // Background using our clip path
            drawPath(
                color = containerColor,
                path = path
            )

            // Current progress indicator
            val offsetY = size.height - (size.height * progress)
            drawRect(
                topLeft = Offset(0f, offsetY),
                size = Size(size.width, size.height - offsetY),
                color = fillColor,
            )

            // Draw icon with padding
            val vectorPadding = 24.dp.toPx()
            // Desired draw size
            val desiredVectorSize = Size(48.dp.toPx(), 48.dp.toPx())
            // Scale vector size if needed
            val vectorSize = if ((desiredVectorSize.width + (vectorPadding * 2)) > size.width) {
                val scale = (size.width - 2 * vectorPadding) / desiredVectorSize.width
                Size(desiredVectorSize.width * scale, desiredVectorSize.height * scale)
            } else {
                desiredVectorSize
            }
            translate(
                left = center.x - (vectorSize.width / 2),
                top = size.height - vectorPadding - vectorSize.height
            ) {
                with(ringBellIcon) {
                    draw(
                        size = vectorSize,
                        colorFilter = ringBellColor
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun VolumeSliderPreview() {
    var progress by remember { mutableFloatStateOf(0.25f) }
    VolumeSlider(
        progress = progress,
        onProgressChange = { progress = it },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        fillColor = SliderColor,
        iconRes = R.drawable.ic_bell_24,
        iconTintColor = OnSliderColor,
        modifier = Modifier.size(100.dp, 250.dp)
    )
}
