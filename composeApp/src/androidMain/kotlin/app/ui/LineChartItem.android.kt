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
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import domain.model.ExpenseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlin.math.roundToInt

@Composable
actual fun LineChart(
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
    val bottomAxis = rememberBottomAxis(
        valueFormatter = vicoLineFormatter,
        guideline = null,
        tick = null,
        itemPlacer = AxisItemPlacer.Horizontal.default(
            spacing = chartDataList.size / 2,
            offset = 20,
            shiftExtremeTicks = true,
            addExtremeLabelPadding = true
        ),
        label = rememberAxisLabelComponent(textSize = FireflyAppTheme.typography.labelMedium.fontSize)
    )
    val startAxisFormatter = CartesianValueFormatter { value, _, _ ->
        "${value.roundToInt()}"
    }
    val startAxis = rememberStartAxis(
        valueFormatter = startAxisFormatter,
        tick = null,
        itemPlacer = AxisItemPlacer.Vertical.count(count = { 5 }),
        label = rememberAxisLabelComponent(textSize = FireflyAppTheme.typography.labelMedium.fontSize),
    )
    val modelProducer = remember { CartesianChartModelProducer.build() }
    LaunchedEffect(chartDataList.size) {
        withContext(Dispatchers.Default) {
            modelProducer.tryRunTransaction {
                lineSeries {
                    series(chartDataList.map { it.expenseAmount })
                }
            }
        }
    }
    val scrollState = rememberVicoScrollState(scrollEnabled = false)
    val zoomState = rememberVicoZoomState(zoomEnabled = false, initialZoom = Zoom.Content)
    CartesianChartHost(
        modifier = modifier,
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                listOf(
                    rememberLineSpec(
                        shader = DynamicShader.color(chartLineColor),
                        thickness = 0.8f.dp,
                        backgroundShader = DynamicShader.verticalGradient(
                            colors = intArrayOf(
                                chartLineColor.copy(0.8f).toArgb(),
                                chartLineColor.copy(0.5f).toArgb()
                            ),
                            positions = null
                        )
                    )
                )
            ),
            startAxis = startAxis,
            bottomAxis = bottomAxis
        ),
        modelProducer = modelProducer,
        zoomState = zoomState,
        scrollState = scrollState,
        marker = marker
    )
}