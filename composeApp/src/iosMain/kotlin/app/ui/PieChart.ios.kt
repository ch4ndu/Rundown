package app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import com.chandu.PieChartWrapper
import platform.UIKit.UIViewController


@Composable
actual fun PieChart(
    modifier: Modifier,
    pieEntriesMap: Map<String, Double>,
    setActiveTag: (String) -> Unit
) {
    val pieWrapper = remember { PieChartWrapper() }
    val controller = remember { pieWrapper.makeViewController() }
    LaunchedEffect(pieEntriesMap) {
        // swift expects nullable parameter here
        val temp: (String?) -> Unit = {
            it?.let(setActiveTag)
        }
        val tempMap: Map<Any?, *> = pieEntriesMap.mapKeys { it.key }
        pieWrapper.updateDataWithPieEntriesMap(pieEntriesMap = tempMap, setActiveTag = temp)
    }
    UIKitViewController(
        modifier = modifier.fillMaxSize(), factory = {
            controller as UIViewController
        },
        update = {
            val temp: (String?) -> Unit = {
                it?.let(setActiveTag)
            }
            val tempMap: Map<Any?, *> = pieEntriesMap.mapKeys { it.key }
            pieWrapper.updateDataWithPieEntriesMap(pieEntriesMap = tempMap, setActiveTag = temp)
        }
    )

}