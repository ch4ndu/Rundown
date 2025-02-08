package app.ui.chartutils

import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import data.database.model.charts.ChartData
import data.database.serializers.DateSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime


class VicoLineXAxisFormatter(private val entries: List<ChartData?>) :
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