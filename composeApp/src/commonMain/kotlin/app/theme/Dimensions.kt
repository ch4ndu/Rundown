package app.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface CommonDimensions {

    /**
     * Common spacing used across most UI
     */
    val listSpacing: Dp
        get() = 12.dp

    /**
     * Horizontal margins for content
     */
    val contentMargin: Dp
        get() = 24.dp

    /**
     * Common vertical padding used across most UI
     */
    val verticalPadding: Dp
        get() = 8.dp
    val childPadding: Dp
        get() = 3.dp
    val horizontalPadding: Dp
        get() = 12.dp
    val borderWidth: Dp
        get() = 2.dp

    val roundedCorner: Dp
        get() = 8.dp

    val cardElevation: Dp
        get() = 8.dp
}

object FireFlyAppDimensions : CommonDimensions