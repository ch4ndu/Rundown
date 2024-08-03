package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.model.MergeMode
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat


/**
 * [dataList] accepts [ExpenseIncomeData] or [ExpenseData]
 */
@Composable
expect fun BarChart(
    modifier: Modifier,
    dataList: List<Any>,
    showPersistedMarkers: Boolean = false,
    mergeMode: MergeMode = MergeMode.Stacked,
    horizontalLineValue: Float = 0f,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>,
    dataSelected: (Any) -> Unit
)