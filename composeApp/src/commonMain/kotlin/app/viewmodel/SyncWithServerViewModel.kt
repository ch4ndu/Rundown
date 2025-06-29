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

package app.viewmodel

import androidx.lifecycle.viewModelScope
import data.AppPref
import di.DispatcherProvider
import domain.usecase.SyncStatus
import domain.usecase.SyncWithServerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

open class SyncWithServerViewModel(
    private val syncWithServerUseCase: SyncWithServerUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val appPref: AppPref
) : BaseViewModel() {

    val syncStatus: MutableStateFlow<SyncState> = MutableStateFlow(SyncState.IDLE)

    open suspend fun startFirstSync() {
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