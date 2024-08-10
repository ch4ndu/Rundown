package app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import app.model.MergeMode
import com.chandu.BarChartWrapper
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import org.lighthousegames.logging.logging
import platform.UIKit.UIViewController

/**
 * [dataList] accepts [ExpenseIncomeData] or [ExpenseData]
 */
private val log = logging()

@Composable
actual fun BarChart(
    modifier: Modifier,
    dataList: List<Any>,
    showPersistedMarkers: Boolean,
    mergeMode: MergeMode,
    horizontalLineValue: Float,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>,
    dataSelected: (Any) -> Unit
) {
    if (dataList.isNotEmpty()) {
        if (dataList[0] is ExpenseIncomeData) {
            val expenseIncomeDataList = dataList.filterIsInstance<ExpenseIncomeData>()

            val barWrapper = remember { BarChartWrapper() }
            val controller = remember { barWrapper.makeViewController() }
            var swiftMap: Map<Any?, *> = remember {
                mapOf<Any?, List<Float>>()
            }

            val temp: (String?) -> Unit = { dateString ->
                log.d { "itemClicked: $dateString" }
                dateString?.let { dateString ->
                    expenseIncomeDataList.firstOrNull { it.date.format(DateSerializer.chartMonthYearFormat) == dateString }
                        ?.let(dataSelected)
                }
            }
            log.d { "dataList:$dataList" }
            LaunchedEffect(dataList) {
                if (expenseIncomeDataList.isNotEmpty()) {
                    if (dataList[0] is ExpenseIncomeData) {
                        log.d { "handling ExpenseIncomeData" }
                        val tempMap: Map<Any?, *> =
                            dataList.filterIsInstance<ExpenseIncomeData>().associate {
                                it.date.format(DateSerializer.chartMonthYearFormat) to listOf(
                                    it.expenseAmount,
                                    it.incomeAmount
                                )
                            }
                        swiftMap = tempMap
                        barWrapper.updateWithExpenseIncomeData(tempMap, temp)
                    }
                }
            }

            UIKitViewController(
                modifier = modifier,
                factory = {
                    controller as UIViewController
                },
                update = {
                    if (expenseIncomeDataList.isNotEmpty()) {
                        val tempMap: Map<Any?, *> =
                            expenseIncomeDataList.associate {
                                it.date.format(DateSerializer.chartMonthYearFormat) to listOf(
                                    it.expenseAmount,
                                    it.incomeAmount
                                )
                            }
                        swiftMap = tempMap
                        barWrapper.updateWithExpenseIncomeData(tempMap, temp)
                    }
                }
            )
        } else {
            Box(modifier = modifier) {
                Text("Not Implemented yet", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}