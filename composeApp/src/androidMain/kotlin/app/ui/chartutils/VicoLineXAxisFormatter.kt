package app.ui.chartutils

import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ChartValues
import data.database.model.charts.ChartData
import data.database.serializers.DateSerializer
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime


class VicoLineXAxisFormatter(private val entries: List<ChartData?>) :
    CartesianValueFormatter {

    override fun format(
        value: Float,
        chartValues: ChartValues,
        verticalAxisPosition: AxisPosition.Vertical?
    ): CharSequence {
        if (entries.isNotEmpty()) {
            val date =
                entries.getOrNull(value.toInt())?.date
                    ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return date.format(DateSerializer.chartMonthYearFormat)
        }
        return ""
    }
}