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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.model.MergeMode
import app.theme.CommonDimensions
import app.theme.FireflyAppTheme
import app.theme.WalletLightGreen
import app.theme.WalletOrange2
import app.ui.AppBar
import app.ui.BarChart
import app.ui.DetailsIconButton
import app.ui.LineChart
import app.ui.ThemedBox
import app.ui.getAspectRatio
import app.ui.isLargeScreen
import app.viewmodel.HomeViewModel
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import getDisplayWithCurrency
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.lighthousegames.logging.logging

private val log = logging()

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    onDailySummaryClick: () -> Unit
) {

    // budgetOverview for last 3 months(barChart with spent and remaining)
    // AccountsOverview for last 3 months(LineChart with selectable account chips)
    // pieChart with category spending. This will need calendar selection

    val dailySpending =
        homeViewModel.dailySpendingFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val dailySpendingAverage =
        homeViewModel.dailySpendingAverage.collectAsStateWithLifecycle(initialValue = null)
    val cashFlowDetails =
        homeViewModel.cashFlowDetails.collectAsStateWithLifecycle(initialValue = emptyList())
    val cashFlowAverages =
        homeViewModel.cashFlowAverages.collectAsStateWithLifecycle(initialValue = hashMapOf())

    val netWorthChartData = homeViewModel.netWorthChartDataFlow.collectAsStateWithLifecycle(
        initialValue = null
    )
    val netWorthChartDataList = homeViewModel.netWorthLineDataSetFlow.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    val listState = rememberLazyListState()
    val dimensions = FireflyAppTheme.dimensions
    val isLargeScreen = isLargeScreen()
    val aspectRatio = remember(isLargeScreen.value) {
        getAspectRatio(isLargeScreen.value)
    }
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            AppBar(title = "Dashboard")
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
                    DailySpendingOverview(
                        dailySpending = dailySpending,
                        dailySpendingAverage = dailySpendingAverage,
                        aspectRatio = aspectRatio,
                        onDailySummaryClick = onDailySummaryClick
                    ) { data ->
                        (data as? ExpenseData)?.let {
                            homeViewModel.updateSelectedExpenseData(it)
                        }
                    }
                }
                item {
                    CashFlowOverview(
                        cashFlowDetails = cashFlowDetails,
                        cashFlowAverages = cashFlowAverages,
                        aspectRatio = aspectRatio
                    )
                }
                val chartData = netWorthChartData.value
                if (chartData != null) {
                    item {
                        NetWorthItem(
                            netWorthChartDataList = netWorthChartDataList,
                            dimensions = dimensions,
                            aspectRatio = aspectRatio
                        )
                    }
                }

                item(contentType = "bottomSpacer") {
                    Spacer(modifier = Modifier.height(dimensions.listSpacing))
                }
            }
        }
    }
}

@Composable
fun DailySpendingOverview(
    dailySpending: State<List<ExpenseData>>,
    dailySpendingAverage: State<Double?>,
    aspectRatio: Float,
    onDailySummaryClick: () -> Unit,
    dataSelected: (Any) -> Unit
) {
    ThemedBox {
        DetailsIconButton(boxScope = this) {
            onDailySummaryClick.invoke()
        }
        Column {
            Text(
                text = "Daily Spending Overview \nLast 30 days",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 6.dp),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio),
                dataList = dailySpending.value,
                dateTimeFormatter = DateSerializer.chartDayMonthFormat
            ) { data ->
                dataSelected.invoke(data)
            }
            if (dailySpendingAverage.value != null) {
                Row(modifier = Modifier.padding(all = 3.dp)) {
                    Text(
                        text = "Average Spending per day: ",
                        textAlign = TextAlign.Center,
                        style = FireflyAppTheme.typography.titleMedium
                    )
                    val expenseText = remember(dailySpendingAverage.value) {
                        (dailySpendingAverage.value
                            ?: 0.0).getDisplayWithCurrency("$")
                    }
                    Text(
                        text = expenseText,
                        style = FireflyAppTheme.typography.titleLarge.copy(
                            color = WalletOrange2
                        )
                    )
                }
            }
        }
    }

}

@Composable
fun CashFlowOverview(
    cashFlowDetails: State<List<ExpenseIncomeData>>,
    cashFlowAverages: State<HashMap<String, Double>>,
    aspectRatio: Float
) {
    ThemedBox {
        Column {
            Text(
                text = "CashFlow Overview",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 6.dp),
                textAlign = TextAlign.Center
            )
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio),
                dataList = cashFlowDetails.value,
                mergeMode = MergeMode.Stacked,
                dateTimeFormatter = DateSerializer.chartMonthYearFormat
            ) {
            }
            if (cashFlowAverages.value["expense"] != null) {
                Row(modifier = Modifier.padding(all = 3.dp)) {
                    Text(
                        text = "Average Expenses: ",
                        textAlign = TextAlign.Center,
                        style = FireflyAppTheme.typography.titleMedium
                    )
                    val expenseText = remember(cashFlowAverages.value) {
                        (cashFlowAverages.value["expense"]
                            ?: 0.0).getDisplayWithCurrency("$")
                    }
                    Text(
                        text = expenseText,
                        style = FireflyAppTheme.typography.titleLarge.copy(
                            color = WalletOrange2
                        )
                    )
                }
                Row(modifier = Modifier.padding(all = 3.dp)) {
                    Text(
                        text = "Average Income: ",
                        textAlign = TextAlign.Center,
                        style = FireflyAppTheme.typography.titleMedium
                    )
                    val incomeText = remember(cashFlowAverages.value) {
                        (cashFlowAverages.value["income"]
                            ?: 0.0).getDisplayWithCurrency("$")
                    }
                    Text(
                        text = incomeText,
                        style = FireflyAppTheme.typography.titleLarge.copy(
                            color = WalletLightGreen
                        )
                    )
                }
            }
        }
    }

}

@Composable
fun NetWorthItem(
    netWorthChartDataList: State<List<ExpenseData>?>,
    dimensions: CommonDimensions,
    aspectRatio: Float
) {
    ThemedBox {
        Text(
            modifier = Modifier
                .padding(
                    vertical = dimensions.verticalPadding
                )
                .align(Alignment.TopCenter),
            text = "Net Worth",
            style = FireflyAppTheme.typography.headlineMedium
        )
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            chartDataList = netWorthChartDataList.value
        )
    }

}