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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.theme.FireflyAppTheme
import app.ui.AppBarWithBack
import app.ui.BarChart
import app.ui.TransactionItem
import app.viewmodel.DailySpendingDetailsViewModel
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun DailySpendingDetailsScreen(
    dailySpendingDetailsViewModel: DailySpendingDetailsViewModel = koinViewModel(),
    onBack: () -> Unit
) {

    val dailySpending =
        dailySpendingDetailsViewModel.dailySpendingFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val transactions =
        dailySpendingDetailsViewModel.transactionsForSelectedExpenseData.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )

    val listState = rememberLazyListState()
    val dimensions = FireflyAppTheme.dimensions
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            AppBarWithBack(title = "Spending") {
                onBack.invoke()
            }
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(dimensions.listSpacing),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(all = dimensions.contentMargin)
            ) {

                item(contentType = "topSpacer") {
                    Spacer(modifier = Modifier.height(dimensions.listSpacing))
                }

                item {
                    Column {
                        Text(text = "Daily Spending Overview")
                        BarChart(
                            modifier = Modifier.height(192.dp),
                            dataList = dailySpending.value,
                            showPersistedMarkers = true,
                            dateTimeFormatter = DateSerializer.chartDayMonthFormat
                        ) { data ->
                            (data as? ExpenseData)?.let {
                                dailySpendingDetailsViewModel.updateSelectedExpenseData(it)
                            }
                        }
                    }
                }

                item(contentType = "transactionSpacer") {
                    Spacer(modifier = Modifier.height(dimensions.listSpacing))
                }

                transactions.value.forEach {
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