package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import app.model.TotalsSummary
import data.enums.ExpenseType

@Composable
expect fun PieChart(
    modifier: Modifier,
    pieEntriesMap: Map<String, Double>,
    setActiveTag: (String) -> Unit
)