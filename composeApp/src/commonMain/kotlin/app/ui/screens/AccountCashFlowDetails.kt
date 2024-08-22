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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.model.MergeMode
import app.theme.FireflyAppTheme
import app.ui.AppBarWithBack
import app.ui.BarChart
import app.ui.ThemedBox
import app.ui.TransactionItem
import app.ui.getAspectRatio
import app.ui.isLargeScreen
import app.ui.isScrollingUp
import app.viewmodel.AccountCashFlowDetailsViewModel
import data.database.serializers.DateSerializer
import domain.model.ExpenseIncomeData
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun AccountCashFlowDetails(
    accountId: String,
    startDate: String,
    endDate: String,
    accountCashFlowDetailsViewModel: AccountCashFlowDetailsViewModel = koinViewModel(),
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        accountCashFlowDetailsViewModel.setAccountId(accountId)
        accountCashFlowDetailsViewModel.setStartDate(startDate)
        accountCashFlowDetailsViewModel.setEndDate(endDate)
    }
    val cashFlowData by accountCashFlowDetailsViewModel.cashFlowData.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val transactions by accountCashFlowDetailsViewModel.transactionsFlow.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val listState = rememberLazyListState()
    val isScrollingUp = listState.isScrollingUp()
    val dimensions = FireflyAppTheme.dimensions
    val isLargeScreen = isLargeScreen()
    val aspectRatio = remember(isLargeScreen.value) {
        getAspectRatio(isLargeScreen.value)
    }

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            AppBarWithBack(title = "CashFlow") {
                onBack.invoke()
            }


            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(dimensions.listSpacing),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(all = dimensions.contentMargin)
//                    .padding(start = dimensions.contentMargin, end = dimensions.contentMargin)
            ) {

                if (cashFlowData.isNotEmpty()) {
                    item(key = "cashFlowOverview") {
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
                                    dataList = cashFlowData,
                                    mergeMode = MergeMode.Stacked,
                                    dateTimeFormatter = DateSerializer.chartMonthYearFormat,
                                    showPersistedMarkers = true
                                ) { data ->
                                    (data as? ExpenseIncomeData)?.let {
                                        accountCashFlowDetailsViewModel.updateSelectedMonth(it.date)
                                    }
                                }
                            }
                        }
                    }

                    item(contentType = "transactionSpacer") {
                        Spacer(modifier = Modifier.height(dimensions.listSpacing))
                    }

                    transactions.forEach {
                        item(
                            key = it.transaction_journal_id,
                            contentType = "transaction"
                        ) {
                            TransactionItem(accountId = accountId, item = it)
                        }
                    }

                    item(contentType = "bottomSpacer") {
                        Spacer(modifier = Modifier.height(dimensions.listSpacing))
                    }
                }

            }
        }
    }
}