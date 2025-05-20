package app.ui.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import app.theme.FireflyAppTheme
import app.theme.HaloBlue
import app.ui.charts.chartutils.VicoLineBottomAxisFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.Zoom
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.multiplatform.common.fill
import domain.model.ExpenseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlin.math.roundToInt


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
        label = rememberAxisLabelComponent(
            style = FireflyAppTheme.typography.labelMedium
                .copy(color = FireflyAppTheme.colorScheme.onSurface)
        )
    )
    val startAxisFormatter = CartesianValueFormatter { _, value, _ ->
        "${value.roundToInt()}"
    }
    val startAxis = VerticalAxis.rememberStart(
        valueFormatter = startAxisFormatter,
        tick = null,
        itemPlacer = VerticalAxis.ItemPlacer.count(count = { 5 }),
        label = rememberAxisLabelComponent(
            style = FireflyAppTheme.typography.labelMedium
                .copy(color = FireflyAppTheme.colorScheme.onSurface)
        ),
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
                                    Brush.verticalGradient(
                                        listOf(
                                            chartLineColor.copy(0.8f),
                                            chartLineColor.copy(0.5f)
                                        ),
                                        tileMode = TileMode.Clamp
                                    )
//                                    ShaderProvider.verticalGradient(
//                                        chartLineColor.copy(0.8f).toArgb(),
//                                        chartLineColor.copy(0.5f).toArgb()
//                                    )
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