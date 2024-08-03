@file:OptIn(ExperimentalSerializationApi::class)

package domain.repository

import data.database.model.accounts.UserInfo
import data.network.AuthorizationInterceptor
import data.network.firefly.OAuthService
import data.network.firefly.SystemInfoService
import data.network.firefly.createSystemInfoService
import data.network.getKtorFit
import domain.AuthState
import io.ktor.client.network.sockets.ConnectTimeoutException
import kotlinx.serialization.ExperimentalSerializationApi
import org.lighthousegames.logging.logging

class AuthRepository(
    private val oAuthService: OAuthService,
    private val activeUser: UserInfo,
    private val userRepository: UserRepository,
) {
    private val log = logging()

    suspend fun refreshTokens(): Boolean {
        val networkCall = oAuthService.getRefreshToken(
            refreshToken = activeUser.refreshToken,
            clientId = activeUser.clientId,
            clientSecret = activeUser.secretKey
        )
        val authResponse = networkCall.body()
        val errorBody = networkCall.errorBody()
        log.d { "authResponse: $authResponse" }
        if (authResponse != null && networkCall.isSuccessful) {
            val accessToken = authResponse.access_token.trim()
            log.d { "oldToken: ${activeUser.accessToken}" }
            log.d { "newToken: $accessToken" }
            val userInfo = activeUser.copy(
                accessToken = accessToken,
                refreshToken = authResponse.refresh_token.trim(),
                tokenExpiry = authResponse.expires_in,
                authMethod = "oauth"
            )
            userRepository.insert(userInfo)
            AuthorizationInterceptor.accessToken = accessToken
            return true
        } else if (errorBody != null) {
            log.d { "refreshTokens-error: $errorBody" }
        }
        return false
    }

    suspend fun tryAuth(
        url: String,
        token: String
    ): AuthState {
        val ktorFit = getKtorFit(baseUrl = url, accessToken = token)

        //TODO fix this once ios compiles
        val systemInfoService = ktorFit.createSystemInfoService()

        try {
            val response = systemInfoService.getSystemInfo()
            val systemInfoModel = response.body()
            val systemData = systemInfoModel?.systemData
            if (systemData != null) {
                val userInfo = UserInfo(
                    baseUrl = url,
                    accessToken = token,
                    activeUser = true,
                    authMethod = "pat"
                )
                userRepository.insert(userInfo)
                val userAttribute =
                    systemInfoService.getCurrentUserInfo().body()?.userData?.userAttributes
                if (userAttribute != null) {
                    val temp = userInfo.copy(userEmail = userAttribute.email ?: "")
                    userRepository.insert(temp)
                }
                return AuthState.Authenticated
            } else {
                return if (response.code == 401) {
                    AuthState.Error("Unauthenticated")
                } else {
                    AuthState.Error("unknown")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return if (e is ConnectTimeoutException) {
                AuthState.Error("Not Connected to network")
            } else {
                AuthState.Error("unknown")
            }
        }

    }
}