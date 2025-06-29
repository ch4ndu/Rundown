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
import di.DispatcherProvider
import di.mockViewModelModule
import di.viewModelModule
import domain.AuthState
import domain.repository.AuthRepository
import domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.lighthousegames.logging.logging

open class AuthViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel() {

    private val log = logging()

    val uiState: MutableStateFlow<AuthScreenState> = MutableStateFlow(AuthScreenState.Idle)

    open fun checkAuth() {
        log.d { "checkAuth" }
        uiState.value = AuthScreenState.InProgress
        viewModelScope.launch(dispatcherProvider.default) {
            if (userRepository.getCurrentActiveUserInfo(true)?.userEmail?.isNotEmpty() == true) {
                uiState.value = AuthScreenState.Authenticated
                log.d { "checkAuth:Authenticated" }
            } else {
                uiState.value = AuthScreenState.Idle
                log.d { "checkAuth:Idle" }
            }
        }
    }

    open fun tryAuth(
        url: String,
        token: String
    ) {
        viewModelScope.launch(dispatcherProvider.default) {
            if (url.isEmpty()) {
                uiState.value = AuthScreenState.Error("Url cannot be empty")
                return@launch
            }
            if (url.endsWith("/")) {
                uiState.value =
                    AuthScreenState.Error("Url cannot have a trailing slash. Valid format is http://192.168.0.1")
                return@launch
            }
            if (token.isEmpty()) {
                uiState.value = AuthScreenState.Error("token cannot be empty")
                return@launch
            }

            uiState.value = AuthScreenState.InProgress
            when (val authState = authRepository.tryAuth(url, token)) {
                is AuthState.Authenticated -> {
//                    unloadKoinModules(networkModule)
//                    loadKoinModules(networkModule)
                    uiState.value = AuthScreenState.Authenticated
                }

                is AuthState.Error -> uiState.value = AuthScreenState.Error(authState.reason)
            }
        }
    }

    fun runDemo() {
        unloadKoinModules(viewModelModule)
        loadKoinModules(mockViewModelModule)
        uiState.value = AuthScreenState.Authenticated
    }
}

sealed class AuthScreenState {
    data object Idle : AuthScreenState()
    data object Authenticated : AuthScreenState()
    data object InProgress : AuthScreenState()
    data class Error(val reason: String) : AuthScreenState()
}