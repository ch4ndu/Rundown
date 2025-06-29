/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

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