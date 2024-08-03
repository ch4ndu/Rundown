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