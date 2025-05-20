package app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.model.TotalsSummary
import data.enums.ExpenseType

@Composable
actual fun PieChart(
    modifier: Modifier,
    pieEntriesMap: Map<String, Double>,
    tagTotal: MutableState<Double>,
    setActiveTag: (String) -> Unit,
    types: List<ExpenseType>
) {
    Box(modifier = modifier) {
        Text("Not Implemented Yet!", modifier = Modifier.align(Alignment.Center))
    }
}