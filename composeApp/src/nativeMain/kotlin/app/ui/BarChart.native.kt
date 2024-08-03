package app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.model.MergeMode
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

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
    Box(modifier = modifier) {
        Text("Not Implemented Yet!", modifier = Modifier.align(Alignment.Center))
    }
}