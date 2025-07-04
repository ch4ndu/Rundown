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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import app.theme.DeltaGekko
import app.theme.GrillRed
import app.theme.WalletLightGreen
import app.theme.WalletOrange2
import domain.model.CategorySpending
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import getDisplayWithCurrency
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlin.math.absoluteValue

fun ExpenseIncomeData.toChartLabel(dateTimeFormatter: DateTimeFormat<LocalDateTime>): AnnotatedString {
    return buildAnnotatedString {
        append(date.format(dateTimeFormatter))
        if (expenseAmount > 0) {
            val expenseString = "\nExpense: ${expenseAmount.toDouble().getDisplayWithCurrency("$")}"
            append(expenseString)
            addStyle(
                style = SpanStyle(color = GrillRed),
                start = length - expenseString.length,
                end = length
            )
        }
        if (incomeAmount > 0) {
            val incomeStr = "\nIncome: ${incomeAmount.toDouble().getDisplayWithCurrency("$")}"
            append(incomeStr)
            addStyle(
                style = SpanStyle(color = DeltaGekko),
                start = length - incomeStr.length,
                end = length
            )
        }
    }
}

fun ExpenseData.toChartLabel(
    showSpendingLabel: Boolean,
    dateTimeFormatter: DateTimeFormat<LocalDateTime>
): AnnotatedString {
    return buildAnnotatedString {
        append(date.format(dateTimeFormatter))
        if (showSpendingLabel) {
            val expenseString =
                "\r\nExpense: ${expenseAmount.toDouble().getDisplayWithCurrency("$")}"
            append(expenseString)
            addStyle(
                style = SpanStyle(color = GrillRed),
                start = length - expenseString.length,
                end = length
            )
        } else {
            val expenseString = "\r\n${expenseAmount.toDouble().getDisplayWithCurrency("$")}"
            append(expenseString)
            addStyle(
                style = SpanStyle(color = GrillRed),
                start = length - expenseString.length,
                end = length
            )
        }
    }
}

fun Double.getTextColorForAmount(): Color {
    return if (this.absoluteValue > this) {
        WalletOrange2
    } else {
        WalletLightGreen
    }
}

fun Float.getTextColorForAmount(): Color {
    return if (this.absoluteValue > this) {
        WalletOrange2
    } else {
        WalletLightGreen
    }
}

fun getAspectRatio(isLargeScreen: Boolean): Float = if (isLargeScreen) 1.75f else 1.25f

fun CategorySpending.getDisplayOverView(): AnnotatedString {
    return buildAnnotatedString {
        if (totalExpenseSum > 0) {
            withStyle(SpanStyle()) {
                append("Expenses: ")
            }
            withStyle(SpanStyle(GrillRed)) {
                append(totalExpenseSum.getDisplayWithCurrency("$"))
            }
        }
        if (totalIncomeSum > 0) {
            withStyle(SpanStyle()) {
                append("\r\nIncome: ")
            }
            withStyle(SpanStyle(DeltaGekko)) {
                append(totalIncomeSum.getDisplayWithCurrency("$"))
            }
        }
        if (totalIncomeSum != 0f && totalExpenseSum != 0f) {
            val netAmount = totalIncomeSum - totalExpenseSum
            if (netAmount > 0) {
                withStyle(SpanStyle()) {
                    append("\r\nNet: ")
                }
                withStyle(SpanStyle(DeltaGekko)) {
                    append(netAmount.getDisplayWithCurrency("$"))
                }
            } else {
                withStyle(SpanStyle()) {
                    append("\r\nNet: ")
                }
                withStyle(SpanStyle(GrillRed)) {
                    append(netAmount.absoluteValue.getDisplayWithCurrency("$"))
                }
            }
        }
    }
}