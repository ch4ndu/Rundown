package app.ui.charts.chartutils

import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime


class VicoLineBottomAxisFormatter(private val entries: List<ExpenseData?>) :
    CartesianValueFormatter {

    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?
    ): CharSequence {
        if (entries.isNotEmpty()) {
            val date =
                entries.getOrNull(value.toInt())?.date
                    ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return date.format(DateSerializer.chartMonthYearFormat)
        }
        return "unknown"
    }
}