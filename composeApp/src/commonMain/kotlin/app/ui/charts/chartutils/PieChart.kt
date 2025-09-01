/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package app.ui.charts.chartutils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.min

/**
 * Data class representing a single slice of the pie chart.
 * @param label The name of the slice, used for display.
 * @param value The numerical value of the slice, used to calculate its proportion.
 * @param color The color of the slice.
 */
data class PieChartData(
    val label: String,
    val value: Float,
    val color: Color
)

/**
 * A composable that displays an interactive pie chart.
 * Slices can be selected by dragging a finger or mouse over them.
 *
 * @param modifier The modifier to be applied to the chart.
 * @param data A list of [PieChartData] points to be displayed as slices.
 * @param onSliceSelected A lambda that is invoked when a slice is selected, providing the selected data point.
 */
@Composable
fun PieChartKmm(
    modifier: Modifier = Modifier,
    data: List<PieChartData>,
    onSliceSelected: (PieChartData?) -> Unit
) {
    // State to hold the index of the currently selected slice. -1 means no selection.
    var selectedIndex by remember { mutableStateOf(-1) }
    val animatedScales = remember {
        data.map { Animatable(1f) }
    }

    // Animate the scale of the selected slice.
    LaunchedEffect(selectedIndex) {
        animatedScales.forEachIndexed { index, animatable ->
            animatable.animateTo(
                targetValue = if (index == selectedIndex) 1.1f else 1.0f,
                animationSpec = tween(durationMillis = 300)
            )
        }
    }

    // Pre-calculate the total value and sweep angles for each slice.
    val totalValue = remember(data) { data.sumOf { it.value.toDouble() }.toFloat() }
    val sweepAngles = remember(data) {
        data.map { 360f * it.value / totalValue }
    }

    // Notify the listener when the selection changes.
    // Using snapshotFlow ensures this is called only when selectedIndex actually changes.
    LaunchedEffect(selectedIndex) {
        snapshotFlow { selectedIndex }
            .collect { index ->
                onSliceSelected(data.getOrNull(index))
            }
    }

    Canvas(
        modifier = modifier
            .pointerInput(data) {
                detectDragGestures(
                    onDragStart = { offset ->
                        // Find and update the selected slice on drag start
                        val angle = getAngleFromOffset(offset, size.width / 2f, size.height / 2f)
                        val newIndex = findSliceIndexForAngle(angle, sweepAngles)
                        selectedIndex = newIndex
                    },
                    onDrag = { change, _ ->
                        // Find and update the selected slice during drag
                        val angle =
                            getAngleFromOffset(change.position, size.width / 2f, size.height / 2f)
                        val newIndex = findSliceIndexForAngle(angle, sweepAngles)
                        selectedIndex = newIndex
                        change.consume()
                    }
                )
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val radius = min(canvasWidth, canvasHeight) / 2f * 0.8f // 80% of half the min dimension
        val center = Offset(canvasWidth / 2f, canvasHeight / 2f)

        var startAngle = -90f // Start from the top

        // Draw each slice
        sweepAngles.forEachIndexed { index, sweepAngle ->
            withTransform({
                // Apply the animated scale transformation for the selected slice
                scale(animatedScales[index].value, pivot = center)
            }) {
                drawArc(
                    color = data[index].color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                )
            }
            startAngle += sweepAngle
        }
    }
}

/**
 * Calculates the angle in degrees for a given touch offset relative to a center point.
 * The angle is adjusted to be in the range [0, 360], starting from the 3 o'clock position
 * and moving clockwise.
 */
private fun getAngleFromOffset(
    offset: Offset,
    centerX: Float,
    centerY: Float
): Float {
    val dx = offset.x - centerX
    val dy = offset.y - centerY
    // atan2 returns the angle in radians from -PI to PI.
    val theta = atan2(dy, dx)
    // Convert radians to degrees using the formula: degrees = radians * 180 / PI
    var angle = (theta * (180.0 / PI)).toFloat()

    // Convert angle to be in the range [0, 360]
    if (angle < 0) {
        angle += 360f
    }
    return angle
}

/**
 * Finds the index of the pie chart slice corresponding to a given angle.
 *
 * @param targetAngle The angle (in degrees) of the touch point.
 * @param sweepAngles A list of the sweep angles for each slice.
 * @return The index of the slice, or -1 if not found.
 */
private fun findSliceIndexForAngle(
    targetAngle: Float,
    sweepAngles: List<Float>
): Int {
    var currentAngle = -90f + 360f // Start from the same position as drawing (-90)
    var cumulativeAngle = 0f

    // We adjust the target angle to match the coordinate system of our drawing logic
    val adjustedTargetAngle = (targetAngle + 90f) % 360f

    sweepAngles.forEachIndexed { index, sweep ->
        cumulativeAngle += sweep
        if (adjustedTargetAngle <= cumulativeAngle) {
            return index
        }
    }

    return -1 // Should not happen if logic is correct
}