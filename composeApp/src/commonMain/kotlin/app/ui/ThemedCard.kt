package app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import app.theme.DialogDarkSurface
import app.theme.FireflyAppTheme
import app.theme.Warm0

@Composable
fun ThemedCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick,
        shape = FireflyAppTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) DialogDarkSurface else Warm0
        ),
        elevation = CardDefaults.cardElevation(),
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = FireflyAppTheme.shapes.large,
                clip = true
            ),
        content = content
    )
}