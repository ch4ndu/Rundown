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

import data.network.firefly.SystemInfoService
import io.ktor.client.network.sockets.ConnectTimeoutException

class HealthCheckRepository(
    private val systemInfoService: SystemInfoService,
) {

    suspend fun getHealthCheck(): HealthCheck {
        try {
            val response = systemInfoService.getSystemInfo()
            val systemInfoModel = response.body()
            val systemData = systemInfoModel?.systemData
            if (systemData != null) {
                return HealthCheck.Connected
            } else {
                if (
                    response.code == 401 &&
                    response.message.equals("Unauthorized", true)
                ) {
                    return HealthCheck.ExpiredToken
                }
            }
        } catch (exception: Exception) {
            return if (exception is ConnectTimeoutException) {
                HealthCheck.Failed
            } else {
                HealthCheck.Unknown
            }
        }
        return HealthCheck.Unknown
    }
}

sealed class HealthCheck {
    data object Unknown : HealthCheck()
    data object Failed : HealthCheck()
    data object ExpiredToken : HealthCheck()
    data object Connected : HealthCheck()
}