package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

@Composable
actual fun isLargeScreen(): State<Boolean> {
    val density = LocalConfiguration.current
    return remember(LocalConfiguration.current) {
        mutableStateOf(density.screenWidthDp > 840)
    }
}

@Composable
actual fun screenHeight(): Int {
    return LocalConfiguration.current.screenHeightDp
}

@Composable
actual fun isMediumScreen(): State<Boolean> {
    val density = LocalConfiguration.current
    return remember(LocalConfiguration.current) {
        mutableStateOf(density.screenWidthDp in 421..840)
    }
}

@Composable
actual fun isSmallScreen(): State<Boolean> {
    val density = LocalConfiguration.current
    return remember(LocalConfiguration.current) {
        mutableStateOf(density.screenWidthDp in 1..420)
    }
}

@Composable
actual fun screenWidth(): Int {
    return LocalConfiguration.current.screenWidthDp
}