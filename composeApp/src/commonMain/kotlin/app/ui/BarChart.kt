package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.model.MergeMode
import app.ui.charts.BarChartKmm
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat


/**
 * [dataList] accepts [ExpenseIncomeData] or [ExpenseData]
 */
@Composable
expect fun BarChartNative(
    modifier: Modifier,
    dataList: List<Any>,
    showPersistedMarkers: Boolean = false,
    mergeMode: MergeMode = MergeMode.Stacked,
    horizontalLineValue: Float = 0f,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>,
    dataSelected: (Any) -> Unit
)


/**
 * [dataList] accepts [ExpenseIncomeData] or [ExpenseData]
 */
@Composable
fun BarChart(
    modifier: Modifier,
    dataList: List<Any>,
    showPersistedMarkers: Boolean = false,
    mergeMode: MergeMode = MergeMode.Stacked,
    horizontalLineValue: Float = 0f,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>,
    dataSelected: (Any) -> Unit
) {
//    BarChartNative(
//        modifier = modifier,
//        dataList = dataList,
//        showPersistedMarkers = showPersistedMarkers,
//        mergeMode = mergeMode,
//        horizontalLineValue = horizontalLineValue,
//        dateTimeFormatter = dateTimeFormatter,
//        dataSelected = dataSelected
//    )
    BarChartKmm(
        modifier = modifier,
        dataList = dataList,
        showPersistedMarkers = showPersistedMarkers,
        mergeMode = mergeMode,
        horizontalLineValue = horizontalLineValue,
        dateTimeFormatter = dateTimeFormatter,
        dataSelected = dataSelected
    )
}