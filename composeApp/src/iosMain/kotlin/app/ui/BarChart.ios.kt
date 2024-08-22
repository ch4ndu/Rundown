@file:OptIn(ExperimentalForeignApi::class)

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
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import org.lighthousegames.logging.logging
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.UIKit.UIViewController

/**
 * dataList accepts [ExpenseIncomeData] or [ExpenseData]
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
            val expenseIncomeDataList = remember(dataList) {
                dataList.filterIsInstance<ExpenseIncomeData>()
            }
            val barWrapper = remember { BarChartWrapper() }
            val controller = remember { barWrapper.makeViewController() }

            val temp: (NSDate?) -> Unit = { dateString ->
                log.d { "itemClicked: $dateString" }
                dateString?.let { dateString ->
                    expenseIncomeDataList.firstOrNull { it.date.toNsDate() == dateString }
                        ?.let(dataSelected)
                }
            }
            log.d { "dataList:$dataList" }
            LaunchedEffect(dataList) {
                if (expenseIncomeDataList.isNotEmpty()) {
                    if (dataList[0] is ExpenseIncomeData) {
                        log.d { "handling ExpenseIncomeData" }
                        val tempMap: Map<Any?, *> = expenseIncomeDataList.associate {
                                it.date.toNsDate() to listOf(
                                    it.expenseAmount,
                                    it.incomeAmount
                                )
                            }
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
                        val tempMap: Map<Any?, *> = expenseIncomeDataList.associate {
                                it.date.toNsDate() to listOf(
                                    it.expenseAmount,
                                    it.incomeAmount
                                )
                            }
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

private fun LocalDateTime.toNsDate(): NSDate? {
    val calendar = NSCalendar.currentCalendar
    val components = NSDateComponents()
    components.year = this.year.convert()
    components.month = this.monthNumber.convert()
    components.day = this.dayOfMonth.convert()
    components.hour = this.hour.convert()
    components.minute = this.minute.convert()
    components.second = this.second.convert()
    return calendar.dateFromComponents(components)
}