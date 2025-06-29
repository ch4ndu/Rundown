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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import app.theme.FireflyAppTheme
import rundown.composeapp.generated.resources.Res
import rundown.composeapp.generated.resources.ic_arrow_right
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