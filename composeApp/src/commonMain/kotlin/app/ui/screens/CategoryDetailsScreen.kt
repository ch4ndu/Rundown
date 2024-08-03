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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.theme.FireflyAppTheme
import app.ui.AppBarWithBack
import app.ui.BarChart
import app.ui.TransactionItem
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
    categoryDetailsViewModel: CategoryDetailsViewModel = koinViewModel(),
    onBack: () -> Unit
) {
//    BackHandler(enabled = true) {
//        onBack.invoke()
//    }
    LaunchedEffect(Unit) {
        categoryDetailsViewModel.setSelectedCategory(selectedCategory)
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
            AppBarWithBack(title = categoryName.value) {
                onBack.invoke()
            }
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(dimensions.listSpacing),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = dimensions.contentMargin, end = dimensions.contentMargin)
            ) {
                item(
                    key = "spendingBarChart",
                    contentType = "barChart"
                ) {
                    BarChart(
                        modifier = Modifier
                            .height(256.dp),
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