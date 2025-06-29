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

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

interface CommonShapes {

    val small: CornerBasedShape
        get() = RoundedCornerShape(4.dp)
    val medium: CornerBasedShape
        get() = RoundedCornerShape(8.dp)
    val large: CornerBasedShape
        get() = RoundedCornerShape(12.dp)

    fun toShapes(): Shapes {
        return Shapes(
            small = small,
            medium = medium,
            large = large
        )
    }
}

object FireflyAppShapes : CommonShapes
