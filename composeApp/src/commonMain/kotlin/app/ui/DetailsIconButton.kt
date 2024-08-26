package app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import app.theme.FireflyAppTheme
import fireflycomposemultiplatform.composeapp.generated.resources.Res
import fireflycomposemultiplatform.composeapp.generated.resources.ic_arrow_right
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun DetailsIconButton(
    boxScope: BoxScope,
    onClick: () -> Unit
) {
    boxScope.apply {
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { onClick.invoke() }
        ) {
            SkippableImage(resource = Res.drawable.ic_arrow_right)
        }
    }
}

@Composable
fun SkippableImage(resource: DrawableResource) {
    Image(
        painter = painterResource(resource = resource),
        contentDescription = "Details",
        colorFilter = ColorFilter.tint(FireflyAppTheme.colorScheme.onSecondaryContainer)
    )
}