package app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

open class BaseViewModel: ViewModel() {

    fun <T> Flow<T>.toStateFlow(
        initial: T,
        started: SharingStarted = SharingStarted.WhileSubscribed(5000),
        scope: CoroutineScope = viewModelScope
    ): StateFlow<T> {
        return this.stateIn(
            scope = scope,
            started = started,
            initialValue = initial
        )
    }
}