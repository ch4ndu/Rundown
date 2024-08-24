package app.viewmodel.mock

import androidx.lifecycle.viewModelScope
import app.viewmodel.SyncState
import app.viewmodel.SyncWithServerViewModel
import data.AppPref
import di.DispatcherProvider
import domain.usecase.SyncWithServerUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.lighthousegames.logging.logging

class MockSyncWithServerViewModel(
    private val syncWithServerUseCase: SyncWithServerUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val appPref: AppPref
) : SyncWithServerViewModel(syncWithServerUseCase, dispatcherProvider, appPref) {
    private val log = logging()

    override suspend fun startFirstSync() {
        log.d { "startFirstSync" }
        viewModelScope.launch(dispatcherProvider.default) {
            delay(10_000)
            syncStatus.value = SyncState.Done
        }
    }
}