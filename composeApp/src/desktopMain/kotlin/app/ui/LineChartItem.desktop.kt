package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import domain.model.ExpenseData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

@Composable
actual fun LineChartNative(
    modifier: Modifier,
    chartDataList: List<ExpenseData>?,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>
) {
}