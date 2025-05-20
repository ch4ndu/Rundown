package app.ui.charts

import app.ui.toChartLabel
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.DefaultCartesianMarker
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

class MarkerLabelFormatter(
    colorCode: Boolean = true,
    private val spendingDataList: List<Any>,
    private val dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartMonthYearFormat,
    private val showSpendingLabel: Boolean
) : DefaultCartesianMarker.ValueFormatter {

    override fun format(
        context: CartesianDrawingContext,
        targets: List<CartesianMarker.Target>
    ): CharSequence {
        val index = targets[0].x.toInt()
        val data = spendingDataList[index]
        if (data is ExpenseIncomeData) {
            return data.toChartLabel(dateTimeFormatter)
        } else if (data is ExpenseData) {
            return data.toChartLabel(showSpendingLabel, dateTimeFormatter)
        }
        return "unknown"
    }
}