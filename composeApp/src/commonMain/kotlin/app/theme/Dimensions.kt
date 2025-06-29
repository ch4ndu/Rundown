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