/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import com.chandu.PieChartWrapper
import data.enums.ExpenseType
import platform.UIKit.UIViewController


@Composable
actual fun PieChart(
    modifier: Modifier,
    pieEntriesMap: Map<String, Double>,
    tagTotal: MutableState<Double>,
    setActiveTag: (String) -> Unit,
    types: List<ExpenseType>
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