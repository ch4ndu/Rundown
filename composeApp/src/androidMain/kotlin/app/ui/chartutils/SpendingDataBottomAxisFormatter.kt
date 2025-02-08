package app.ui.chartutils

import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat


class SpendingDataBottomAxisFormatter(
    private val entries: List<Any>,
    private val dateTimeFormatter: DateTimeFormat<LocalDateTime>
) : CartesianValueFormatter {

    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?
    ): CharSequence {
        if (entries.isNotEmpty()) {
            val entry = entries.getOrNull(value.toInt())
            if (entry is ExpenseIncomeData) {
                return entry.date.format(dateTimeFormatter)
            } else if (entry is ExpenseData) {
                return entry.date.format(dateTimeFormatter)
            }
            return "unknown"
        }
        return "unknown"
    }
}