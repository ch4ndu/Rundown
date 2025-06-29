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