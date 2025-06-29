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