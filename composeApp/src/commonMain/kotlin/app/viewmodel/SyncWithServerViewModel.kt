package app.viewmodel

import androidx.lifecycle.viewModelScope
import data.AppPref
import di.DispatcherProvider
import domain.usecase.SyncStatus
import domain.usecase.SyncWithServerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SyncWithServerViewModel(
    private val syncWithServerUseCase: SyncWithServerUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val appPref: AppPref
) : BaseViewModel() {

    val syncStatus: MutableStateFlow<SyncState> = MutableStateFlow(SyncState.IDLE)

    suspend fun startFirstSync() {
        viewModelScope.launch(dispatcherProvider.default) {
            if (!appPref.isFirstSyncPending().first()) {
                if (syncStatus.value == SyncState.IDLE) {
                    syncStatus.value = SyncState.InProgress
                    val temp = syncWithServerUseCase.invoke()
                    when (temp) {
                        is SyncStatus.SyncFailed -> {
                            appPref.setFirstSyncDone()
                            //TODO show error message
                            syncStatus.value = SyncState.Done
                        }

                        is SyncStatus.SyncSuccess -> {
                            appPref.setFirstSyncDone()
                            syncStatus.value = SyncState.Done
                        }
                    }
                }
            } else {
                syncStatus.value = SyncState.Done
            }
        }
    }
}

sealed class SyncState {
    data object IDLE : SyncState()
    data object InProgress : SyncState()
    data object Done : SyncState()
}