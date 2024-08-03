package app.ui.chartutils

import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ChartValues
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat


class SpendingDataBottomAxisFormatter(
    private val entries: List<Any>,
    private val dateTimeFormatter: DateTimeFormat<LocalDateTime>
) :
    CartesianValueFormatter {

    override fun format(
        value: Float,
        chartValues: ChartValues,
        verticalAxisPosition: AxisPosition.Vertical?
    ): CharSequence {
        if (entries.isNotEmpty()) {
            val entry = entries.getOrNull(value.toInt())
            if (entry is ExpenseIncomeData) {
                return entry.date.format(dateTimeFormatter)
            } else if (entry is ExpenseData) {
                return entry.date.format(dateTimeFormatter)
            }
            return ""
        }
        return ""
    }
}