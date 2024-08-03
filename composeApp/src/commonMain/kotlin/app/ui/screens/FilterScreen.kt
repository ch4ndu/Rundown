@file:OptIn(ExperimentalMaterial3Api::class)

package app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.theme.DarkGrey
import app.theme.FireflyAppTheme
import app.theme.HaloBlue
import app.theme.WalletBlue
import app.ui.screenHeight
import data.database.serializers.DateSerializer
import domain.atBeginningOfMonth
import domain.millis
import domain.minusMonths
import domain.minusYearsAtBeginning
import domain.minusYearsAtEnd
import domain.model.DateRange
import domain.withStartOfDay
import domain.withStartOfYear
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.lighthousegames.logging.logging

private val log = logging()

@Composable
fun FilterScreen(
    dateRange: DateRange,
    onClose: () -> Unit,
    onSave: (DateRange) -> Unit
) {
    val tabIndex = remember { mutableIntStateOf(0) }
    val tabs = listOf("Quick Filters", "Custom")

    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).withStartOfDay()
    val lastThreeMonths = DateRange(today.minusMonths(2).atBeginningOfMonth(), today)
    val lastSixMonths = DateRange(today.minusMonths(5).atBeginningOfMonth(), today)
    val thisYear = DateRange(today.withStartOfYear(), today)
    val lastYear = DateRange(
        today.minusYearsAtBeginning(1),
        today.minusYearsAtEnd(1)
    )
    val lastYearToDate = DateRange(
        today.minusYearsAtBeginning(1),
        today
    )
    val lastTwoYears = DateRange(
        today.minusYearsAtBeginning(2),
        today
    )
    val lastThreeYears = DateRange(
        today.minusYearsAtBeginning(3),
        today
    )
    val filterList = listOf(
        Pair("Last 3 Months", lastThreeMonths),
        Pair("Last 6 Months", lastSixMonths),
        Pair("Current Year", thisYear),
        Pair("Last Year", lastYear),
        Pair("Last Year To Date", lastYearToDate),
        Pair("Last 2 Years", lastTwoYears),
        Pair("Last 3 YEars", lastThreeYears)
    )

    val quickFilterSelected = remember {
        mutableStateOf(filterList[0])
    }
    val screenHeight = screenHeight()
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = dateRange.startDate.millis(),
        initialSelectedEndDateMillis = dateRange.endDate.millis(),
        yearRange = IntRange(currentYear - 3, currentYear)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(100.dp, max = (screenHeight * 4 / 5).dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
            TextButton(
                onClick = {
                    if (tabIndex.intValue == 0) {
                        log.d { "selected:${quickFilterSelected.value.second.getDebugDisplay()}" }
                        onSave.invoke(quickFilterSelected.value.second)
                    } else {
                        val selectedDateRange = DateRange(
                            startDate = Instant.fromEpochMilliseconds(
                                state.selectedStartDateMillis ?: 1
                            )
                                .toLocalDateTime(
                                    TimeZone.currentSystemDefault()
                                ),
                            endDate = Instant.fromEpochMilliseconds(
                                state.selectedEndDateMillis ?: 1
                            )
                                .toLocalDateTime(
                                    TimeZone.currentSystemDefault()
                                )
                        )
                        log.d {
                            "chandu-dateRange:start-${
                                selectedDateRange.startDate.format(
                                    DateSerializer.sendToApiFormat
                                )
                            }"
                        }
                        log.d {
                            "chandu-dateRange:end-${
                                selectedDateRange.endDate.format(
                                    DateSerializer.sendToApiFormat
                                )
                            }"
                        }
                        onSave.invoke(selectedDateRange)
                    }
                },
                enabled = state.selectedEndDateMillis != null && state.selectedStartDateMillis != null
            ) {
                Text(text = "Apply")
            }
        }
        val dimensions = FireflyAppTheme.dimensions

        TabRow(selectedTabIndex = tabIndex.value) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex.value == index,
                    onClick = { tabIndex.value = index },
                    selectedContentColor = HaloBlue,
                    unselectedContentColor = DarkGrey
                )
            }
        }
        when (tabIndex.value) {
            0 -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(1f)
                ) {
//                    Spacer(modifier = Modifier.height(16.dp))
                    filterList.forEachIndexed { index, filterPair: Pair<String, DateRange> ->
                        val isSelected = filterPair.first == quickFilterSelected.value.first
                        Column(
                            Modifier
                                .background(
                                    color =
                                    if (isSelected) WalletBlue
                                    else Color.Transparent
                                )
                                .clickable {
                                    quickFilterSelected.value = filterPair
                                }
                                .fillMaxWidth(1f)
                                .padding(
                                    horizontal = dimensions.contentMargin
                                )
                                .padding(all = dimensions.listSpacing)
                        ) {
                            Text(
                                text = filterPair.first,
                                modifier = Modifier.padding(horizontal = dimensions.verticalPadding),
                                style = FireflyAppTheme.typography.titleMedium
                            )
                            Text(
                                text = filterPair.second.getFilterDisplay(),
                                modifier = Modifier.padding(horizontal = dimensions.verticalPadding),
                                style = FireflyAppTheme.typography.bodyMedium
                                    .copy(fontWeight = FontWeight.Thin)
                            )
                        }
                    }
                }
            }

            1 -> {
                DateRangePicker(
                    state = state,
                    modifier = Modifier.weight(1f),
                    showModeToggle = true
                )
            }
        }
    }
}