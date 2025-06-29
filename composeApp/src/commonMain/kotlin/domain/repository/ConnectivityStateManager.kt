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

package domain.repository

import data.network.AuthorizationInterceptor
import di.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.lighthousegames.logging.logging

class ConnectivityStateManager(
    private val dispatcherProvider: DispatcherProvider,
    private val healthCheckRepository: HealthCheckRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val coroutineScope: CoroutineScope
) {
    private val log = logging()
    private val _isConnectedToServer = MutableStateFlow<HealthCheck?>(HealthCheck.Unknown)

    val isConnectedToServer = _isConnectedToServer
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(500),
            initialValue = HealthCheck.Unknown
        )

    private var monitorConnectivityJob: Job? = null
        set(value) {
            log.d { "Setting new job:$value" }
            field?.cancel()
            field = value
        }

    private var trackAccessTokenJob: Job? = null
        set(value) {
            log.d { "Setting new job:$value" }
            field?.cancel()
            field = value
        }

    fun startMonitoringConnectivity() {
        log.d { "startMonitoringConnectivity" }
        monitorConnectivityJob = scheduleCheckServerConnection()
        trackAccessTokenJob = trackUserToken()
    }

    fun stopMonitoringConnectivity() {
        log.d { "stopMonitoringConnectivity" }
        monitorConnectivityJob = null
        trackAccessTokenJob = null
    }

    private fun scheduleCheckServerConnection(): Job {
        return coroutineScope.async(dispatcherProvider.default) {
            while (isActive) {
                _isConnectedToServer.value = isConnectedToServer()
                log.d { "isConnectedToServer-${isConnectedToServer.value}" }
                delay(5 * 60_000)
            }
        }
    }

    private fun trackUserToken(): Job {
        return coroutineScope.launch(dispatcherProvider.default) {
            userRepository.getCurrentActiveUserInfoFlow(isActive = true).filterNotNull().collect {
                AuthorizationInterceptor.accessToken = it.accessToken
            }
        }
    }

    private suspend fun isConnectedToServer(): HealthCheck {
        return coroutineScope.async(dispatcherProvider.default) {
            healthCheckRepository.getHealthCheck()
        }.await()
    }

    suspend fun refreshTokens(): Boolean {
        val user = userRepository.getCurrentActiveUserInfo(isActive = true)
        if (user != null && user.authMethod == "pat") {
            log.d { "using PAT for authentication. No need to refresh" }
            return true
        }
        return authRepository.refreshTokens()
    }

    init {
        monitorConnectivityJob = scheduleCheckServerConnection()
    }
}