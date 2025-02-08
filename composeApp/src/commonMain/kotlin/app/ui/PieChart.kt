package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import data.enums.ExpenseType

@Composable
expect fun PieChart(
    modifier: Modifier,
    pieEntriesMap: Map<String, Double>,
    tagTotal: MutableState<Double>,
    setActiveTag: (String) -> Unit,
    types: List<ExpenseType>
)