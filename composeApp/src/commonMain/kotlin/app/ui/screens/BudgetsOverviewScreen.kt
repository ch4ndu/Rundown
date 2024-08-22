package app.ui.screens

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.theme.FireflyAppTheme
import app.ui.BarChart
import app.ui.DetailsIconButton
import app.ui.ThemedBox
import app.viewmodel.BudgetListOverviewViewModel
import data.database.serializers.DateSerializer
import org.koin.compose.koinInject

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
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(dimensions.listSpacing),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(all = dimensions.contentMargin)
            ) {
                item { Spacer(modifier = Modifier.height(20.dp)) }
                if (budgetSpending.value.isEmpty()) {
                    item {
                        Text(text = "No Budgets to display!!")
                    }
                }
                budgetSpending.value.forEach { budgetSpending ->
                    item { Spacer(modifier = Modifier.height(dimensions.listSpacing)) }
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
                                val overview = ""
                                if (overview.isNotEmpty()) {
                                    Text(
                                        text = overview,
                                        modifier = Modifier.padding(6.dp),
                                        maxLines = 3
                                    )

                                }
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