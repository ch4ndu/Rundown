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
import app.viewmodel.AuthScreenState
import app.viewmodel.AuthViewModel
import di.DispatcherProvider
import domain.repository.AuthRepository
import domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MockAuthViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val dispatcherProvider: DispatcherProvider,
) : AuthViewModel(userRepository, authRepository, dispatcherProvider) {

    override fun checkAuth() {
        uiState.value = AuthScreenState.InProgress
        viewModelScope.launch(dispatcherProvider.default) {
            delay(2_000)
            uiState.value = AuthScreenState.Idle
        }
    }

    override fun tryAuth(
        url: String,
        token: String
    ) {
        viewModelScope.launch(dispatcherProvider.default) {
            uiState.value = AuthScreenState.InProgress
            delay(2_000)
            uiState.value = AuthScreenState.Authenticated
        }
    }
}