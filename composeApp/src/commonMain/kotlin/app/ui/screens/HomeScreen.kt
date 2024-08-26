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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.model.MergeMode
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

    // DailySummary(last 7 days - expenses)
    // cashFlow(stacked barChart with expenses and income for last 3 months)

    // budgetOverview for last 3 months(barChart with spent and remaining)
    // AccountsOverview for last 3 months(LineChart with selectable account chips)
    // categoryOverview(barChart with dropDown to select categories)

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
                                (data as? ExpenseData)?.let {
                                    homeViewModel.updateSelectedExpenseData(it)
                                }
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
                item {
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
                val chartData = netWorthChartData.value
                if (chartData != null) {
                    item {
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
//                        val firstEntry = chartData.entries.entries.getOrNull(0)
//                        val firstAmount = firstEntry?.value ?: 0.0
//                        val startDateDisplay =
//                            dateRange.startDate.toString(JodaDateAdapter.balanceDisplayFormat)
//                        val lastEntry =
//                            chartData.entries.entries.getOrNull(chartData.entries.entries.count() - 1)
//                        val lastAmount = lastEntry?.value ?: 0.0
//                        val endDateDisplay =
//                            dateRange.endDate.toString(JodaDateAdapter.balanceDisplayFormat)
//                        Row(
//                            modifier = Modifier.padding(
//                                vertical = 5.dp,
//                                horizontal = dimensions.contentMargin
//                            )
//                        ) {
//                            Text(
//                                text = "Balance on ${startDateDisplay}: ",
//                                style = FireflyAppTheme.typography.titleMedium
//                            )
//                            Text(
//                                text = firstAmount.getDisplayWithCurrency("$"),
//                                style = FireflyAppTheme.typography.titleLarge.copy(
//                                    color = firstAmount.getTextColorForAmount()
//                                )
//                            )
//                        }
//                        Row(
//                            modifier = Modifier
//                                .padding(bottom = 5.dp)
//                                .padding(horizontal = dimensions.contentMargin)
//                        ) {
//                            Text(
//                                text = "Balance on ${endDateDisplay}: ",
//                                style = FireflyAppTheme.typography.titleMedium
//                            )
//                            Text(
//                                text = lastAmount.getDisplayWithCurrency("$"),
//                                style = FireflyAppTheme.typography.titleLarge.copy(
//                                    color = lastAmount.getTextColorForAmount()
//                                )
//                            )
//                        }
                        }
                    }
                }

                item(contentType = "bottomSpacer") {
                    Spacer(modifier = Modifier.height(dimensions.listSpacing))
                }
            }
        }
    }
}