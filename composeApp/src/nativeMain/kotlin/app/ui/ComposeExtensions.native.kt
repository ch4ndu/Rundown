@file:OptIn(ExperimentalComposeUiApi::class)

package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@Composable
actual fun isLargeScreen(): State<Boolean> {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    return remember(density, windowInfo) {
        mutableStateOf(with(density) { windowInfo.containerSize.width.toDp() > 840.dp })
    }
}

@Composable
actual fun screenHeight(): Int {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val height = with(density) {
        windowInfo.containerSize.height.toDp()
    }
    return height.value.toInt()
}

@Composable
actual fun isMediumScreen(): State<Boolean> {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    return remember(density, windowInfo) {
        mutableStateOf(with(density) { windowInfo.containerSize.width.toDp() in 421.dp..840.dp })
    }
}

@Composable
actual fun isSmallScreen(): State<Boolean> {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    return remember(density, windowInfo) {
        mutableStateOf(with(density) { windowInfo.containerSize.width.toDp() < 420.dp })
    }
}

@Composable
actual fun screenWidth(): Int {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val width = with(density) {
        windowInfo.containerSize.width.toDp()
    }
    return width.value.toInt()
}