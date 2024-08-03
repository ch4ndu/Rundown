package app.ui

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
    val pieWrapper = remember {
        val wrapper = PieChartWrapper()
//        val temp: (String?) -> Unit = {
//            it?.let(setActiveTag)
//        }
//        val tempMap: Map<Any?, *> = pieEntriesMap.mapKeys { it.key }
//        wrapper.updateDataWithPieEntriesMap(pieEntriesMap = tempMap, setActiveTag = temp)
        wrapper
    }
    val controller = remember { pieWrapper.makeViewController() }
    LaunchedEffect(Unit) {
        val temp: (String?) -> Unit = {
            it?.let(setActiveTag)
        }
        val tempMap: Map<Any?, *> = pieEntriesMap.mapKeys { it.key }
        pieWrapper.updateDataWithPieEntriesMap(pieEntriesMap = tempMap, setActiveTag = temp)
    }
    UIKitViewController(
        modifier = modifier, factory = {
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