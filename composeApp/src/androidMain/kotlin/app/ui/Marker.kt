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

package app.ui

import amountDecimalFormat
import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Layout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import app.theme.FireflyAppTheme
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shadow
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shape.markerCorneredShape
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.layer.CartesianLayerDimensions
import com.patrykandpatrick.vico.core.cartesian.layer.CartesianLayerMargins
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.component.Shadow
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import data.database.serializers.DateSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

@Composable
internal fun rememberSpendingDataMarker(
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
    spendingDataList: List<Any>,
    showIndicator: Boolean = true,
    showSpendingLabel: Boolean = true,
    dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartMonthYearFormat
): CartesianMarker {
    val labelBackgroundShape = markerCorneredShape(all = CorneredShape.Corner.Rounded)
    val labelBackground =
        rememberShapeComponent(
            shape = labelBackgroundShape,
            fill = Fill(color = MaterialTheme.colorScheme.surface.toArgb()),
            shadow =
            shadow(
                radius = LABEL_BACKGROUND_SHADOW_RADIUS_DP.dp,
                y = LABEL_BACKGROUND_SHADOW_DY_DP.dp
            ),
        )
    val label =
        rememberTextComponent(
            color = MaterialTheme.colorScheme.onSurface,
            background = labelBackground,
            padding = insets(8.dp, 8.dp),
            typeface = Typeface.MONOSPACE,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
            lineCount = 3,
            textSize = FireflyAppTheme.typography.bodyMedium.fontSize
        )
    val indicatorFrontComponent =
        rememberShapeComponent(
            shape = CorneredShape.Pill,
            fill = Fill(color = MaterialTheme.colorScheme.surface.toArgb())
        )
    val indicatorCenterComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicatorRearComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicator =
        LayeredComponent(
            back = indicatorRearComponent,
            front =
            LayeredComponent(
                back = indicatorCenterComponent,
                front = indicatorFrontComponent,
                padding = insets(5.dp),
            ),
            padding = insets(10.dp),
        )
    val guideline = rememberAxisGuidelineComponent()
    return remember(label, labelPosition, indicator, guideline, spendingDataList) {
        @SuppressLint("RestrictedApi")
        object : DefaultCartesianMarker(
            label = label,
            labelPosition = labelPosition,
            indicator =
            if (showIndicator) {
                { color ->
                    LayeredComponent(
                        back =
                        ShapeComponent(
                            Fill(ColorUtils.setAlphaComponent(color, 38)),
                            CorneredShape.Pill
                        ),
                        front =
                        LayeredComponent(
                            back =
                            ShapeComponent(
                                fill = Fill(color),
                                shape = CorneredShape.Pill,
                                shadow = Shadow(radiusDp = 12f, color = color),
                            ),
                            front = indicatorFrontComponent,
                            padding = insets(5.dp),
                        ),
                        padding = insets(10.dp),
                    )
                }
            } else {
                null
            },
            guideline = guideline,
            indicatorSizeDp = 36f,
            valueFormatter = MarkerLabelFormatter(
                spendingDataList = spendingDataList,
                showSpendingLabel = showSpendingLabel,
                dateTimeFormatter = dateTimeFormatter
            )
        ) {
            override fun updateLayerMargins(
                context: CartesianMeasuringContext,
                layerMargins: CartesianLayerMargins,
                layerDimensions: CartesianLayerDimensions,
                model: CartesianChartModel,
            ) {
                with(context) {
                    val baseShadowMarginDp =
                        CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER * LABEL_BACKGROUND_SHADOW_RADIUS_DP
                    var topMargin = (baseShadowMarginDp - LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    var bottomMargin = (baseShadowMarginDp + LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    when (labelPosition) {
                        LabelPosition.Top,
                        LabelPosition.AbovePoint -> topMargin += label.getHeight(context) + tickSizeDp.pixels
                        LabelPosition.Bottom,
//                        LabelPosition.BelowPoint -> bottomMargin += label.getHeight(context) + tickSizeDp.pixels
                        LabelPosition.AroundPoint,

                        LabelPosition.BelowPoint -> {}
                    }
                    layerMargins.ensureValuesAtLeast(top = topMargin, bottom = bottomMargin)
                }
            }
        }
    }
}

@Composable
internal fun rememberMarker(
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = markerCorneredShape(CorneredShape.Corner.Rounded)
    val labelBackground =
        rememberShapeComponent(
            shape = labelBackgroundShape,
            fill = Fill(color = MaterialTheme.colorScheme.surface.toArgb()),
            shadow = Shadow(
                radiusDp = LABEL_BACKGROUND_SHADOW_RADIUS_DP,
                yDp = LABEL_BACKGROUND_SHADOW_DY_DP
            )
        )
//            .setShadow(
//                radius = LABEL_BACKGROUND_SHADOW_RADIUS_DP,
//                dy = LABEL_BACKGROUND_SHADOW_DY_DP,
//                applyElevationOverlay = true,
//            )
    val label =
        rememberTextComponent(
            color = MaterialTheme.colorScheme.onSurface,
            background = labelBackground,
            padding = insets(8.dp, 4.dp),
            typeface = Typeface.MONOSPACE,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
//            textSize = FireflyAppTheme.typography.bodyLarge.fontSize
            textSize = 50.sp
        )
    val indicatorFrontComponent =
        rememberShapeComponent(
            shape = CorneredShape.Pill,
            fill = Fill(color = MaterialTheme.colorScheme.surface.toArgb()),
//            color = MaterialTheme.colorScheme.surface
        )
    val indicatorCenterComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicatorRearComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicator =
        LayeredComponent(
            back = indicatorRearComponent,
            front = LayeredComponent(
                back = indicatorCenterComponent,
                front = indicatorFrontComponent,
                padding = insets(5.dp)
            )
        )
    val guideline = rememberAxisGuidelineComponent()
    return remember(label, labelPosition, indicator, showIndicator, guideline) {
        object : DefaultCartesianMarker(
            label = label,
            labelPosition = labelPosition,
            indicator = if (showIndicator) {
                { indicator }
            } else null,
            indicatorSizeDp = 8f,
//            setIndicatorColor =
//            if (showIndicator) {
//                { color ->
//                    indicatorRearComponent.color = color.copyColor(alpha = .15f)
//                    indicatorCenterComponent.color = color
//                    indicatorCenterComponent.setShadow(radius = 12f, color = color)
//                }
//            } else {
//                null
//            },
            guideline = guideline,
        ) {
//            override fun getInsets(
//                context: CartesianMeasureContext,
//                outInsets: Insets,
//                horizontalDimensions: HorizontalDimensions,
//            ) {
//                with(context) {
//                    super.getInsets(context, outInsets, horizontalDimensions)
//                    val baseShadowInsetDp =
//                        CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER * LABEL_BACKGROUND_SHADOW_RADIUS_DP
//                    outInsets.top += (baseShadowInsetDp - LABEL_BACKGROUND_SHADOW_DY_DP).pixels
//                    outInsets.bottom += (baseShadowInsetDp + LABEL_BACKGROUND_SHADOW_DY_DP).pixels
//                }
//            }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 4f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f
private const val CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 1.4f