package app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import app.theme.DialogDarkSurface
import app.theme.FireflyAppTheme
import app.theme.Warm0

@Composable
fun ThemedBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(4.dp, shape = FireflyAppTheme.shapes.medium)
            .shadow(4.dp, shape = FireflyAppTheme.shapes.medium)
            .clip(FireflyAppTheme.shapes.medium)
            .background(
                color = if (isSystemInDarkTheme()) DialogDarkSurface else Warm0
            )
            .padding(6.dp)
    ) {
        content.invoke(this)
    }

}