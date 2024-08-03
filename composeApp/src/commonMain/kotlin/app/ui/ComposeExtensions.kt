@file:OptIn(ExperimentalComposeUiApi::class)

package app.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import domain.model.DateRange
import kotlinx.datetime.LocalDateTime

val areDateRangesEquivalent = { old: DateRange, new: DateRange ->
    old.startDate.compareTo(new.startDate) == 0 && old.endDate.compareTo(new.endDate) == 0
}

val areDatesEquivalent = { old: LocalDateTime, new: LocalDateTime ->
    old.compareTo(new) == 0
}

@Composable
expect fun isLargeScreen(): State<Boolean>

@Composable
expect fun screenHeight(): Int

@Composable
expect fun screenWidth(): Int

@Composable
expect fun isMediumScreen(): State<Boolean>

@Composable
expect fun isSmallScreen(): State<Boolean>

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(this.firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}