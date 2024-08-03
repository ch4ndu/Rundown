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
import getDisplayWithCurrency
import kotlin.math.absoluteValue

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

fun getAspectRatio(isLargeScreen: Boolean): Float = if (isLargeScreen) 1.75f else 2f

fun CategorySpending.getDisplayOverView(): AnnotatedString {
    return buildAnnotatedString {
        if (expenseSum > 0) {
            withStyle(SpanStyle()) {
                append("Expenses: ")
            }
            withStyle(SpanStyle(GrillRed)) {
                append(expenseSum.getDisplayWithCurrency("$"))
            }
        }
        if (incomeSum > 0) {
            withStyle(SpanStyle()) {
                append("\r\nIncome: ")
            }
            withStyle(SpanStyle(DeltaGekko)) {
                append(incomeSum.getDisplayWithCurrency("$"))
            }
        }
        if (incomeSum != 0f && expenseSum != 0f) {
            val netAmount = incomeSum - expenseSum
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