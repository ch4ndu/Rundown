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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import app.theme.FireflyAppTheme
import app.theme.HaloBlue
import app.ui.chartutils.VicoLineBottomAxisFormatter
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import domain.model.ExpenseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlin.math.roundToInt

@Composable
actual fun LineChartNative(
    modifier: Modifier,
    chartDataList: List<ExpenseData>?,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>
) {
    if (chartDataList != null ) {
        VicoLineChart(
            modifier = modifier,
            chartDataList = chartDataList,
            dateTimeFormatter = dateTimeFormatter
        )
    }
}

@Composable
fun VicoLineChart(
    modifier: Modifier,
    chartDataList: List<ExpenseData>,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>
) {
    val marker = rememberSpendingDataMarker(
        spendingDataList = chartDataList,
        showSpendingLabel = false,
        dateTimeFormatter = dateTimeFormatter
    )
    val vicoLineFormatter = remember(chartDataList) {
        VicoLineBottomAxisFormatter(chartDataList)
    }
    val chartLineColor = HaloBlue
    val bottomAxis = HorizontalAxis.rememberBottom(
        valueFormatter = vicoLineFormatter,
        guideline = null,
        tick = null,
        itemPlacer = HorizontalAxis.ItemPlacer.aligned(
            spacing = { chartDataList.size / 2 },
            offset = { 20 },
            shiftExtremeLines = true,
            addExtremeLabelPadding = true
        ),
        label = rememberAxisLabelComponent(textSize = FireflyAppTheme.typography.labelMedium.fontSize)
    )
    val startAxisFormatter = CartesianValueFormatter { _, value, _ ->
        "${value.roundToInt()}"
    }
    val startAxis = VerticalAxis.rememberStart(
        valueFormatter = startAxisFormatter,
        tick = null,
        itemPlacer = VerticalAxis.ItemPlacer.count(count = { 5 }),
        label = rememberAxisLabelComponent(textSize = FireflyAppTheme.typography.labelMedium.fontSize),
    )
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(chartDataList.size) {
        withContext(Dispatchers.Default) {
            modelProducer.runTransaction {
                lineSeries {
                    series(chartDataList.map { it.expenseAmount })
                }
            }
        }
    }
    val scrollState = rememberVicoScrollState(scrollEnabled = false)
    val zoomState = rememberVicoZoomState(zoomEnabled = false, initialZoom = Zoom.Content)
    val minY = chartDataList.minBy { it.expenseAmount }.expenseAmount.toDouble()
    val maxY = chartDataList.maxBy { it.expenseAmount }.expenseAmount.toDouble()
    val rangeProvider = CartesianLayerRangeProvider.fixed(minY = minY, maxY = maxY)
    CartesianChartHost(
        modifier = modifier,
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(fill(chartLineColor.copy(0.8f))),
                        areaFill =
                        LineCartesianLayer.AreaFill.single(
                            fill(
                                ShaderProvider.verticalGradient(
                                chartLineColor.copy(0.8f).toArgb(),
                                    chartLineColor.copy(0.5f).toArgb()
                                )
                            )
                        )
                    ),
                    LineCartesianLayer.rememberLine(
                        LineCartesianLayer.LineFill.single(
                            fill(
                                chartLineColor.copy(0.5f)
                            )
                        )
                    ),
                ),
                pointSpacing = 0.8f.dp,
                rangeProvider = rangeProvider
            ),
            startAxis = startAxis,
            bottomAxis = bottomAxis,
            marker = marker
        ),
        modelProducer = modelProducer,
        zoomState = zoomState,
        scrollState = scrollState,
    )
}