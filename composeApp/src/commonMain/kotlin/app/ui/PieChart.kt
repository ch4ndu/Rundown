package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PieChart(
    modifier: Modifier,
    pieEntriesMap: Map<String, Double>,
    setActiveTag: (String) -> Unit
)