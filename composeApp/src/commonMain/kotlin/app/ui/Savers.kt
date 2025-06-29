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

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import data.database.serializers.DateSerializer
import domain.model.DateRange
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

object Savers {
    val DateRange: Saver<MutableState<DateRange>, List<String>> = Saver(
        save = {
            listOf(
                it.value.startDate.format(DateSerializer.isoFormat),
                it.value.endDate.format(DateSerializer.isoFormat)
            )
        },
        restore = {
            mutableStateOf(
                DateRange(
                    startDate = LocalDateTime.parse(it[0], DateSerializer.isoFormat),
                    endDate = LocalDateTime.parse(it[1], DateSerializer.isoFormat)
                )
            )
        }
    )
}