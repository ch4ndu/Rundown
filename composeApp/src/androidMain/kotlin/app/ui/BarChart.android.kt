package app.ui

import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import app.model.MergeMode
import app.theme.DeltaGekko
import app.theme.FireflyAppTheme
import app.theme.GrillRed
import app.ui.chartutils.SpendingDataBottomAxisFormatter
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.decoration.rememberHorizontalLine
import com.patrykandpatrick.vico.compose.cartesian.fullWidth
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.shape.Shape
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import org.lighthousegames.logging.logging
import kotlin.math.roundToInt
private val log = logging()
/**
 * [dataList] accepts [ExpenseIncomeData] or [ExpenseData]
 */
@Composable
actual fun BarChart(
    modifier: Modifier,
    dataList: List<Any>,
    showPersistedMarkers: Boolean,
    mergeMode: MergeMode,
    horizontalLineValue: Float,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>,
    dataSelected: (Any) -> Unit
) {
    val barChartMergeMode = remember(mergeMode) {
        when(mergeMode) {
            MergeMode.Grouped -> ColumnCartesianLayer.MergeMode.Grouped
            MergeMode.Stacked -> ColumnCartesianLayer.MergeMode.Stacked
        }
    }
    log.d { "chandu-spendingDataList size: ${dataList.size}" }
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val isSpendingData = remember(dataList) {
        mutableStateOf(true)
    }

    val isLargeData = remember(dataList) {
        mutableStateOf(dataList.size > 15)
    }

    val persistentMarkers = rememberSaveable(dataList) {
        mutableStateOf(emptyMap<Float, CartesianMarker>())
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
                persistentMarkers.value = mapOf(targets[0].x to marker)
                dataSelected.invoke(dataList[selectedPosition])
            }

            override fun onUpdated(
                marker: CartesianMarker,
                targets: List<CartesianMarker.Target>
            ) {
                log.d { "onUpdated:$targets" }
                log.d { "onMarkerShown:isSpendingData ${isSpendingData.value}" }
                val selectedPosition = targets[0].x.toInt()
                persistentMarkers.value = mapOf(targets[0].x to marker)
                dataSelected.invoke(dataList[selectedPosition])
                val color =
                    (targets[0] as? ColumnCartesianLayerMarkerTarget)?.columns?.get(0)?.color
                if (color != null) {
                    if (color == DeltaGekko.toArgb()) {
                        log.d { "color is green" }
                    } else if (color == GrillRed.toArgb()) {
                        log.d { "color is red" }
                    }
                }
            }
        }
    }
    LaunchedEffect(dataList) {
        if (dataList.isNotEmpty()) {
            withContext(Dispatchers.Default) {
                log.d { "chandu-running transaction" }
                modelProducer.tryRunTransaction {
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
    val marker = rememberSpendingDataMarker(
        spendingDataList = dataList,
        dateTimeFormatter = dateTimeFormatter
    )
    val scrollState = rememberVicoScrollState(scrollEnabled = isLargeData.value)
    val zoomState =
        rememberVicoZoomState(zoomEnabled = isLargeData.value, initialZoom = Zoom.Content)

    val horizontalLine = rememberComposeHorizontalLine(horizontalLineValue)

    val startAxisFormatter by remember(dataList) {
        mutableStateOf(CartesianValueFormatter { value, _, _ -> "${value.roundToInt()}" })
    }
    val bottomAxisFormatter by remember(dataList) {
        mutableStateOf(SpendingDataBottomAxisFormatter(dataList, dateTimeFormatter))
    }
    val startAxis = rememberStartAxis(
        valueFormatter = startAxisFormatter,
        tick = null,
        guideline = null,
        itemPlacer = AxisItemPlacer.Vertical.count(count = { 3 }),
        label = rememberAxisLabelComponent(textSize = FireflyAppTheme.typography.labelMedium.fontSize)
    )
    val bottomAxis = rememberBottomAxis(
        guideline = null,
        tick = null,
        valueFormatter = bottomAxisFormatter,
        itemPlacer = remember {
            AxisItemPlacer.Horizontal.default(
                spacing = 2,
                addExtremeLabelPadding = true
            )
        },
        label = rememberAxisLabelComponent(textSize = FireflyAppTheme.typography.labelMedium.fontSize)
    )
    val expenseLineComponent = rememberLineComponent(
        color = GrillRed,
        thickness = 16.dp,
        shape = Shape.rounded(allPercent = 40),
    )
    val incomeLineComponent = rememberLineComponent(
        color = DeltaGekko,
        thickness = 16.dp,
        shape = Shape.rounded(allPercent = 40),
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
            persistentMarkers = if (showPersistedMarkers) persistentMarkers.value else null,
            decorations = if(horizontalLineValue>0f) listOf( horizontalLine) else null
        ),
        modelProducer = modelProducer,
        zoomState = zoomState,
        scrollState = scrollState,
        modifier = modifier,
        marker = marker,
        horizontalLayout = HorizontalLayout.fullWidth(),
        markerVisibilityListener = markerVisibilityChangeListener,
    )
}


@Composable
private fun rememberComposeHorizontalLine(horizontalLine: Float): HorizontalLine {
    val color = Color(-2893786)
    return rememberHorizontalLine(
        y = { horizontalLine },
        line = rememberLineComponent(color, 2f.dp),
        labelComponent =
        rememberTextComponent(
            background = rememberShapeComponent(Shape.Pill, color),
            padding =
            Dimensions.of(
                8f.dp,
                2f.dp,
            ),
            margins = Dimensions.of(4f.dp),
            typeface = Typeface.MONOSPACE,
        ),
    )
}