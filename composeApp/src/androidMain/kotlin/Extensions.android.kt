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

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.theme.DeltaGekko
import app.theme.GrillRed
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue

val amountDecimalFormat = DecimalFormat("$###,###.##").apply { minimumFractionDigits = 2 }
val formatter: NumberFormat get() = NumberFormat.getInstance(Locale.getDefault())

actual fun Double.getDisplayWithCurrency(currencySymbol: String): String {
    val formattedBalance = formatter.format(this.roundTo2Digits().absoluteValue)
    val isNegative =
        this.div(this.absoluteValue) == -1.0
    val negativeText = if (isNegative) "-" else ""
    return "$negativeText$currencySymbol${formattedBalance}"
}

fun ExpenseIncomeData.toChartLabel(dateTimeFormatter: DateTimeFormat<LocalDateTime>): SpannableStringBuilder {
    val builder = SpannableStringBuilder()
    builder.append(date.format(dateTimeFormatter))
    if (expenseAmount > 0) {
        builder.append(
            "\r\nExpense: ${expenseAmount.toDouble().getDisplayWithCurrency("$")}",
            ForegroundColorSpan(GrillRed.toArgb()),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    if (incomeAmount > 0) {
        builder.append(
            "\r\nIncome: ${incomeAmount.toDouble().getDisplayWithCurrency("$")}",
            ForegroundColorSpan(DeltaGekko.toArgb()),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return builder
}

fun ExpenseData.toChartLabel(
    showSpendingLabel: Boolean,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>
): SpannableStringBuilder {
    val builder = SpannableStringBuilder()
    builder.append(date.format(dateTimeFormatter))
    if (showSpendingLabel) {
        builder.append(
            "\r\nExpense: ${expenseAmount.toDouble().getDisplayWithCurrency("$")}",
            ForegroundColorSpan(GrillRed.toArgb()),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    } else {
        builder.append(
            "\r\n${expenseAmount.toDouble().getDisplayWithCurrency("$")}",
            ForegroundColorSpan(if (expenseAmount > 0) DeltaGekko.toArgb() else GrillRed.toArgb()),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return builder
}

@Composable
actual fun BackHandler(onBack: () -> Unit) {
    BackHandler(enabled = true, onBack = onBack)
}

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    context: CoroutineContext,
    initialValue: T
): State<T> =
    collectAsStateWithLifecycle(context = context, initialValue = initialValue)