package app.ui

import com.patrykandpatrick.vico.core.cartesian.CartesianDrawContext
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarkerValueFormatter
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import toChartLabel
import java.text.DecimalFormat

class MarkerLabelFormatter(
    decimalFormat: DecimalFormat,
    colorCode: Boolean = true,
    private val spendingDataList: List<Any>,
    private val dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartMonthYearFormat,
    private val showSpendingLabel: Boolean
) : DefaultCartesianMarkerValueFormatter(decimalFormat, colorCode) {

    override fun format(
        context: CartesianDrawContext,
        targets: List<CartesianMarker.Target>
    ): CharSequence {
        val index = targets[0].x.toInt()
        val data = spendingDataList[index]
        if (data is ExpenseIncomeData) {
            return data.toChartLabel(dateTimeFormatter)
        } else if (data is ExpenseData) {
            return data.toChartLabel(showSpendingLabel, dateTimeFormatter)
        }
        return super.format(context, targets)
    }
}