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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.model.MergeMode
import app.theme.DeltaGekko
import app.theme.GrillRed
import app.ui.charts.chartutils.SpendingDataBottomAxisFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.Zoom
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.component.rememberLineComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.data.ExtraStore
import com.patrykandpatrick.vico.multiplatform.common.fill
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import org.lighthousegames.logging.logging
import kotlin.math.roundToInt

private val log = logging()


@Composable
fun BarChartKmm(
    modifier: Modifier,
    dataList: List<Any>,
    showPersistedMarkers: Boolean,
    mergeMode: MergeMode,
    horizontalLineValue: Float,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>,
    dataSelected: (Any) -> Unit,
) {
    val barChartMergeMode = remember(mergeMode) {
        when (mergeMode) {
            MergeMode.Grouped -> ColumnCartesianLayer.MergeMode.Grouped()
            MergeMode.Stacked -> ColumnCartesianLayer.MergeMode.Stacked
        }
    }
    log.d { "spendingDataList size: ${dataList.size}" }
    val modelProducer = remember { CartesianChartModelProducer() }
    val isSpendingData = remember(dataList) {
        mutableStateOf(true)
    }

    val isLargeData = remember(dataList) {
        mutableStateOf(dataList.size > 15)
    }
    val marker = rememberSpendingDataMarker(
        spendingDataList = dataList,
        dateTimeFormatter = dateTimeFormatter
    )

    val selectedXTarget = rememberSaveable { mutableStateOf<Double?>(null) }
    // Based on selected X value, define a persistent marker
    val persistentMarker = remember(selectedXTarget.value) {
        val persistentMarkerScope: CartesianChart.PersistentMarkerScope.(ExtraStore) -> Unit = {
            selectedXTarget.value?.let { marker at it }
        }
        persistentMarkerScope
    }
    val markerVisibilityChangeListener = remember(dataList) {
        object : CartesianMarkerVisibilityListener {
            override fun onHidden(marker: CartesianMarker) {
                log.d { "markerHidden$marker" }
            }

            override fun onShown(
                marker: CartesianMarker,
                targets: List<CartesianMarker.Target>
            ) {
                log.d { "onShown:$targets" }
                val selectedPosition = targets[0].x.toInt()
                log.d { "onMarkerShown:selectedPosition $selectedPosition" }
                log.d { "onMarkerShown:isSpendingData ${isSpendingData.value}" }
                selectedXTarget.value = targets.firstOrNull()?.x
                dataSelected.invoke(dataList[selectedPosition])
            }

            override fun onUpdated(
                marker: CartesianMarker,
                targets: List<CartesianMarker.Target>
            ) {
                log.d { "onUpdated:$targets" }
                log.d { "onMarkerShown:isSpendingData ${isSpendingData.value}" }
                val selectedPosition = targets[0].x.toInt()
                selectedXTarget.value = targets.firstOrNull()?.x
                dataSelected.invoke(dataList[selectedPosition])
                val color =
                    (targets[0] as? ColumnCartesianLayerMarkerTarget)?.columns?.get(0)?.color
                if (color != null) {
                    if (color == DeltaGekko) {
                        log.d { "color is green" }
                    } else if (color == GrillRed) {
                        log.d { "color is red" }
                    }
                }
            }
        }
    }
    LaunchedEffect(dataList) {
        if (dataList.isNotEmpty()) {
            withContext(Dispatchers.Default) {
                log.d { "running transaction" }
                modelProducer.runTransaction {
                    columnSeries {
                        if (dataList[0] is ExpenseIncomeData) {
                            isSpendingData.value = true
                            val expenseIncomeDataList =
                                dataList.filterIsInstance<ExpenseIncomeData>()
                            series(expenseIncomeDataList.map { it.expenseAmount })
                            series(expenseIncomeDataList.map { it.incomeAmount })
                        } else if (dataList[0] is ExpenseData) {
                            isSpendingData.value = false
                            val expenseDataList = dataList.filterIsInstance<ExpenseData>()
                            series(expenseDataList.map { it.expenseAmount })
                        }
                    }
                }
            }
        }
    }
    val scrollState = rememberVicoScrollState(scrollEnabled = isLargeData.value)
    val zoomState =
        rememberVicoZoomState(zoomEnabled = isLargeData.value, initialZoom = Zoom.Content)

    val horizontalLine = rememberComposeHorizontalLine(horizontalLineValue)

    val startAxisFormatter by remember(dataList) {
        mutableStateOf(CartesianValueFormatter { _, value, _ -> "${value.roundToInt()}" })
    }
    val bottomAxisFormatter by remember(dataList) {
        mutableStateOf(SpendingDataBottomAxisFormatter(dataList, dateTimeFormatter))
    }
    val startAxis = VerticalAxis.rememberStart(
        valueFormatter = startAxisFormatter,
        tick = null,
        guideline = null,
        itemPlacer = VerticalAxis.ItemPlacer.count(count = { 3 }),
        label = rememberAxisLabelComponent(
            style = MaterialTheme.typography.labelMedium
                .copy(color = MaterialTheme.colorScheme.onSurface),
        )
    )
    val bottomAxis = HorizontalAxis.rememberBottom(
        guideline = null,
        tick = null,
        valueFormatter = bottomAxisFormatter,
        itemPlacer = remember {
            HorizontalAxis.ItemPlacer.aligned(spacing = { 2 }, addExtremeLabelPadding = true)
        },
        label = rememberAxisLabelComponent(
            style = MaterialTheme.typography.labelMedium
                .copy(color = MaterialTheme.colorScheme.onSurface)
        )
    )
    val expenseLineComponent = rememberLineComponent(
        fill = fill(color = GrillRed),
        thickness = 16.dp,
        shape = CorneredShape.rounded(allPercent = 40),
    )
    val incomeLineComponent = rememberLineComponent(
        fill = fill(color = DeltaGekko),
        thickness = 16.dp,
        shape = CorneredShape.rounded(allPercent = 40),
    )
    val columnSeries = remember(isSpendingData.value) {
        mutableStateOf(
            when (isSpendingData.value) {
                true -> {
                    ColumnCartesianLayer.ColumnProvider.series(
                        expenseLineComponent,
                        incomeLineComponent
                    )
                }

                false -> {
                    ColumnCartesianLayer.ColumnProvider.series(expenseLineComponent)
                }
            }
        )
    }
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = columnSeries.value,
                    mergeMode = { barChartMergeMode }
                ),
                startAxis = startAxis,
                bottomAxis = bottomAxis,
                persistentMarkers = if (showPersistedMarkers) persistentMarker else null,
                decorations = if (horizontalLineValue > 0f) listOf(horizontalLine) else emptyList(),
                marker = marker,
                markerVisibilityListener = markerVisibilityChangeListener
            ),
        modelProducer = modelProducer,
        zoomState = zoomState,
        scrollState = scrollState,
        modifier = modifier,
    )
}


@Composable
private fun rememberComposeHorizontalLine(horizontalLine: Float): HorizontalLine {
    val color = Color(-2893786)
    return HorizontalLine(
        y = { horizontalLine.toDouble() },
        line = rememberLineComponent(fill = fill(color), 2.dp),
        labelComponent =
            rememberTextComponent(
                background = rememberShapeComponent(
                    shape = CorneredShape.Pill,
                    fill = fill(color = color)
                ),
                padding = Insets(8.dp, 2.dp),
                margins = Insets(4.dp),
            ),
    )
}