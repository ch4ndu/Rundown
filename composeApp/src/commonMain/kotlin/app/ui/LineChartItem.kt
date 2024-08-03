package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

@Composable
expect fun LineChart(
    modifier: Modifier,
    chartDataList: List<ExpenseData>?,
    dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartDayMonthYearFormat
)