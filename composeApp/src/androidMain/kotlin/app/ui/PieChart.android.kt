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

import android.content.Context
import android.widget.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import app.theme.Cyan
import app.theme.DeltaGekko
import app.theme.EnglishRed
import app.theme.FireflyAppTheme
import app.theme.Green
import app.theme.GrillRed
import app.theme.HaloBlue
import app.theme.OrangeSpice
import app.theme.WalletBlue
import app.theme.WalletGreen
import app.theme.WalletOrange
import app.theme.WalletRed
import app.theme.WalletYellow
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import data.enums.ExpenseType

@Composable
actual fun PieChart(
    modifier: Modifier,
    pieEntriesMap: Map<String, Double>,
    tagTotal: MutableState<Double>,
    setActiveTag: (String) -> Unit,
    types: List<ExpenseType>
) {
    val surfaceColor = FireflyAppTheme.colorScheme.surface.toArgb()
    val textSize = FireflyAppTheme.typography.labelMedium.fontSize.value
    val textColor = FireflyAppTheme.colorScheme.onSurface.toArgb()
    val entries = mutableListOf<PieEntry>()
    for (entry in pieEntriesMap.entries) {
        entries.add(PieEntry(entry.value.toFloat(), entry.key))
    }
    val pieDataSet = PieDataSet(entries, "")

    val colors = intArrayOf(
        WalletBlue.toArgb(),
        WalletOrange.toArgb(),
        WalletGreen.toArgb(),
        WalletRed.toArgb(),
        WalletYellow.toArgb(),
        Cyan.toArgb(),
        DeltaGekko.toArgb(),
        EnglishRed.toArgb(),
        Green.toArgb(),
        GrillRed.toArgb(),
        HaloBlue.toArgb(),
        OrangeSpice.toArgb()
    )
    pieDataSet.colors = colors.toList()
    pieDataSet.sliceSpace = 4f
    pieDataSet.selectionShift = 16f

    pieDataSet.setDrawValues(false)
//    pieDataSet.valueTextColor = textColor
//    pieDataSet.valueTextSize = 16f
//    pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
//    pieDataSet.valueLinePart1OffsetPercentage = 80f
//    pieDataSet.valueLinePart1Length = 0.8f
//    pieDataSet.valueLinePart2Length = 0.8f
//    pieDataSet.valueLineColor = textColor

    val pieData = PieData(pieDataSet)
    val highlightEntry = remember {
        mutableStateOf<Highlight?>(null)
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            com.github.mikephil.charting.charts.PieChart(context).apply {
                ViewCompat.setNestedScrollingEnabled(this, true)
                data = pieData
                setBackgroundColor(surfaceColor)
                setHoleColor(surfaceColor)
                setTouchEnabled(true)
                setDrawSlicesUnderHole(true)
                setDrawEntryLabels(false)
                description.isEnabled = false
                setCenterTextColor(textColor)
                setCenterTextSize(textSize)

//                holeRadius = 0f
//                transparentCircleRadius = 0f
                holeRadius = 25f
                transparentCircleRadius = 35f
                isHighlightPerTapEnabled = true
                setDrawMarkers(false)
                isRotationEnabled = false

                legend.isEnabled = false
//                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
//                legend.orientation = Legend.LegendOrientation.HORIZONTAL
//                legend.setDrawInside(false)
//                legend.xEntrySpace = 7f
//                legend.yEntrySpace = 0f
//                legend.yOffset = 0f
//                legend.isWordWrapEnabled = true
//                legend.textColor = textColor

                setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(
                        e: Entry?,
                        h: Highlight?
                    ) {
                        if (e is PieEntry) {
                            setActiveTag.invoke(e.label)
                            tagTotal.value = e.value.toDouble()
                            highlightEntry.value = h
                        }
                    }

                    override fun onNothingSelected() {
                    }
                })
                highlightValue(highlightEntry.value)
            }
        }
    ) {
        it.data = pieData
        it.invalidate()
        try {
            (it as? Chart<PieData>)?.highlightValue(highlightEntry.value)
        } catch (exception: IndexOutOfBoundsException) {
            highlightEntry.value = null
        }
    }
}



fun PlatformView(viewData: String): @Composable () -> Unit {
    return {
        AndroidView(
            factory = { context: Context ->
                Button(context)
            },
            update = { view ->
                view.isEnabled = false
            }
        )
    }
}