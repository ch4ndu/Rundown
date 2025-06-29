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

@file:OptIn(ExperimentalMaterial3Api::class)

package app.ui.screens

import BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.theme.FireflyAppTheme
import app.ui.BarChart
import app.ui.DetailsIconButton
import app.ui.Savers
import app.ui.ThemedBox
import app.viewmodel.BudgetListOverviewViewModel
import collectAsStateWithLifecycle
import data.database.serializers.DateSerializer
import domain.atBeginningOfMonth
import domain.currentDate
import domain.minusMonths
import domain.model.DateRange
import domain.withEndOfMonthAtEndOfDay
import domain.withStartOfDay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.lighthousegames.logging.logging

private val log = logging()

@Composable
fun BudgetListOverviewScreen(
    budgetListOverviewViewModel: BudgetListOverviewViewModel = koinInject()
) {
    val listState = rememberLazyListState()
    val dimensions = FireflyAppTheme.dimensions
    val budgetSpending =
        budgetListOverviewViewModel.budgetListOverviewFlow.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false,
            confirmValueChange = { sheetValue ->
                log.d { "confirmValueChange:sheetValue:$sheetValue" }
                true
            }
        )
    )

    val selectedDateRange = rememberSaveable(saver = Savers.DateRange) {
        mutableStateOf(
            DateRange.getDefaultRange()
        )
    }
    LaunchedEffect(selectedDateRange.value) {
        budgetListOverviewViewModel.setDateRange(selectedDateRange.value)
    }
    BackHandler {
        if (modalSheetState.bottomSheetState.currentValue == SheetValue.Expanded) {
            coroutineScope.launch {
                modalSheetState.bottomSheetState.hide()
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = modalSheetState,
        sheetContent = {
            FilterScreen(
                dateRange = selectedDateRange.value,
                onClose = {
                    coroutineScope.launch { modalSheetState.bottomSheetState.hide() }
                },
                onSave = {
                    selectedDateRange.value = it
                    coroutineScope.launch {
                        modalSheetState.bottomSheetState.hide()
                    }
                }
            )
        },
        sheetSwipeEnabled = true,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(BottomSheetDefaults.SheetPeekHeight)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = selectedDateRange.value.getFilterDisplay()
                )
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                ShowTopBarWithCalendar(modalSheetState = modalSheetState, title = "Budgets")
            },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(dimensions.listSpacing),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(all = dimensions.contentMargin)
                ) {
                    if (budgetSpending.value.isEmpty()) {
                        item {
                            Text(text = "No Budgets to display!!")
                        }
                    }
                    budgetSpending.value.forEach { budgetSpending ->
                        item(
                            key = budgetSpending.budget.name,
                            contentType = "overview"
                        ) {
                            val amount = budgetSpending.budget.amount ?: 0
                            ThemedBox(
                                modifier = Modifier.fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                DetailsIconButton(
                                    boxScope = this,
                                ) {
                                }
                                Column {
                                    Text(
                                        text = "Budget: ${budgetSpending.budget.name}",
                                        modifier = Modifier.padding(6.dp)
                                    )
//                                val overview = budgetSpending.getDisplayOverView()
//                                    val overview = ""
//                                    if (overview.isNotEmpty()) {
//                                        Text(
//                                            text = overview,
//                                            modifier = Modifier.padding(6.dp),
//                                            maxLines = 3
//                                        )
//
//                                    }
                                    BarChart(
                                        dataList = budgetSpending.expenseIncomeDataList,
                                        modifier = Modifier
                                            .height(256.dp),
                                        horizontalLineValue = amount.toFloat(),
                                        dateTimeFormatter = DateSerializer.chartMonthYearFormat,
                                        dataSelected = {}
                                    )
                                }
                            }
                        }

                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }
}