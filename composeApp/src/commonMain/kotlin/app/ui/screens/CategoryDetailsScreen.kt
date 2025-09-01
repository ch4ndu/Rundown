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

@file:OptIn(KoinExperimentalAPI::class)

package app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.theme.FireflyAppTheme
import app.ui.AppBarWithBack
import app.ui.BarChart
import app.ui.TransactionItem
import app.ui.getAspectRatio
import app.ui.isLargeScreen
import app.viewmodel.CategoryDetailsViewModel
import data.database.serializers.DateSerializer
import domain.model.ExpenseIncomeData
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.lighthousegames.logging.logging

private val log = logging()

@Composable
fun CategoryDetailsScreen(
    selectedCategory: String,
    startDate: String,
    endDate: String,
    categoryDetailsViewModel: CategoryDetailsViewModel = koinViewModel(),
    onBack: () -> Unit
) {
//    BackHandler(enabled = true) {
//        onBack.invoke()
//    }
    val isLargeScreen = isLargeScreen()
    val aspectRatio = remember(isLargeScreen.value) {
        getAspectRatio(isLargeScreen.value)
    }
    LaunchedEffect(Unit) {
        categoryDetailsViewModel.setSelectedCategory(selectedCategory)
        categoryDetailsViewModel.setDateRange(startDate, endDate)
    }
    val dimensions = FireflyAppTheme.dimensions
    val categorySpending =
        categoryDetailsViewModel.categorySpending.collectAsStateWithLifecycle(
            initialValue = null
        )
    val categoryName =
        categoryDetailsViewModel.selectedCategory.collectAsStateWithLifecycle(initialValue = "")
    val transactionsForCategory =
        categoryDetailsViewModel.transactionsForCategory.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )
    val spendingDataList = categorySpending.value?.expenseIncomeData
    LaunchedEffect(Unit) {
        if (spendingDataList?.isNotEmpty() == true) {
            spendingDataList[0].let {
                categoryDetailsViewModel.updateSelectedSpendingData(it)
            }
        }
    }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            AppBarWithBack(title = categoryName.value, backClicked = onBack)
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(dimensions.listSpacing),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(all = dimensions.contentMargin)
            ) {
                item(
                    key = "spendingBarChart",
                    contentType = "barChart"
                ) {
                    BarChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(aspectRatio),
                        dataList = spendingDataList ?: emptyList(),
                        showPersistedMarkers = true,
                        dateTimeFormatter = DateSerializer.chartMonthYearFormat,
                        dataSelected = { data ->
                            if (data is ExpenseIncomeData) {
                                categoryDetailsViewModel.updateSelectedSpendingData(data)
                            }
                        }
                    )
                }
                item(contentType = "transactionSpacer") {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                transactionsForCategory.value.forEach {
                    item(
                        key = it.transaction_journal_id,
                        contentType = "transaction"
                    ) {
                        TransactionItem(accountId = "0", item = it)
                    }
                }

                item(contentType = "bottomSpacer") {
                    Spacer(modifier = Modifier.height(dimensions.listSpacing))
                }
            }
        }
    }
}