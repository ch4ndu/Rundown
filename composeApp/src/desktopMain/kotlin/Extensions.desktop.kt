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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow
import java.text.NumberFormat
import java.util.Locale
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue

val formatter: NumberFormat get() = NumberFormat.getInstance(Locale.getDefault())

actual fun Double.getDisplayWithCurrency(currencySymbol: String): String {
    val formattedBalance = formatter.format(this.roundTo2Digits().absoluteValue)
    val isNegative =
        this.div(this.absoluteValue) == -1.0
    val negativeText = if (isNegative) "-" else ""
    return "$negativeText$currencySymbol${formattedBalance}"
}

@Composable
actual fun BackHandler(onBack: () -> Unit) {

}

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    context: CoroutineContext,
    initialValue: T
): State<T> = collectAsState(context)