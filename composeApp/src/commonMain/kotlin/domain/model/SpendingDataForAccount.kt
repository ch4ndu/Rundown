package domain.model

import androidx.compose.runtime.Stable

@Stable
data class SpendingDataForAccount(
    val incomeInsight: List<InsightChartData?>,
    val transferInsight: List<InsightChartData?>,
    val expenseInsight: List<InsightChartData?>,
)