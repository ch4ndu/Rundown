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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue


actual fun Double.getDisplayWithCurrency(currencySymbol: String): String {
    val formatter = NSNumberFormatter()
    val isNegative = this.div(this.absoluteValue) == -1.0
    val negativeText = if (isNegative) "-" else ""
    formatter.minimumFractionDigits = 0u
    formatter.maximumFractionDigits = 2u
    formatter.numberStyle = 1u //Decimal
    return """$negativeText$currencySymbol${formatter.stringFromNumber(NSNumber(this.absoluteValue))!!}"""
}

@Composable
actual fun BackHandler(onBack: () -> Unit) {
}

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    context: CoroutineContext,
    initialValue: T
): State<T> = collectAsState(context)