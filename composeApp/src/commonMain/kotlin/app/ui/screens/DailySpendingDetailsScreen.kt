@file:OptIn(KoinExperimentalAPI::class)

package app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
                    .fillMaxSize()
                    .padding(
                        start = dimensions.contentMargin,
                        end = dimensions.contentMargin
                    )
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