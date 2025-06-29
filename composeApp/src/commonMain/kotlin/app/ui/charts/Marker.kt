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

package app.ui.charts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.DefaultCartesianMarker.LabelPosition
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.multiplatform.common.Fill
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.LayeredComponent
import com.patrykandpatrick.vico.multiplatform.common.component.ShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape
import com.patrykandpatrick.vico.multiplatform.common.shape.MarkerCorneredShape
import data.database.serializers.DateSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

@Composable
internal fun rememberSpendingDataMarker(
    labelPosition: LabelPosition = LabelPosition.Top,
    spendingDataList: List<Any>,
    showIndicator: Boolean = true,
    showSpendingLabel: Boolean = true,
    dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartMonthYearFormat
): CartesianMarker {
    val labelBackgroundShape = MarkerCorneredShape(CorneredShape.Corner.Rounded)
    val labelBackground =
        rememberShapeComponent(
            fill = Fill(MaterialTheme.colorScheme.background),
            shape = labelBackgroundShape,
            strokeFill = Fill(MaterialTheme.colorScheme.background),
            strokeThickness = 1.dp,
        )
    val label =
        rememberTextComponent(
            style =
                TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                ),
            padding = Insets(8.dp, 4.dp),
            background = labelBackground,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
            lineCount = 4
        )
    val indicatorFrontComponent =
        rememberShapeComponent(Fill(MaterialTheme.colorScheme.surface), CorneredShape.Pill)
    val guideline = rememberAxisGuidelineComponent()
    return rememberDefaultCartesianMarker(
        label = label,
        labelPosition = labelPosition,
        valueFormatter = MarkerLabelFormatter(
            spendingDataList = spendingDataList,
            showSpendingLabel = showSpendingLabel,
            dateTimeFormatter = dateTimeFormatter
        ),
        indicator =
            if (showIndicator) {
                { color ->
                    LayeredComponent(
                        back = ShapeComponent(Fill(color.copy(alpha = 0.15f)), CorneredShape.Pill),
                        front =
                            LayeredComponent(
                                back = ShapeComponent(
                                    fill = Fill(color),
                                    shape = CorneredShape.Pill
                                ),
                                front = indicatorFrontComponent,
                                padding = Insets(5.dp),
                            ),
                        padding = Insets(10.dp),
                    )
                }
            } else {
                null
            },
        indicatorSize = 36.dp,
        guideline = guideline,
    )
}