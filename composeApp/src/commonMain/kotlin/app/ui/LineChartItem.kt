package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.ui.charts.VicoLineChart
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

@Composable
expect fun LineChartNative(
    modifier: Modifier,
    chartDataList: List<ExpenseData>?,
    dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartDayMonthYearFormat
)

@Composable
fun LineChart(
    modifier: Modifier,
    chartDataList: List<ExpenseData>?,
    dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartDayMonthYearFormat
) {
    if (chartDataList != null) {
        VicoLineChart(
            modifier = modifier,
            chartDataList = chartDataList,
            dateTimeFormatter = dateTimeFormatter
        )
    }
}