package domain.repository

import data.AppPref
import data.database.dao.UserInfoDao
import data.database.model.accounts.UserInfo
import data.database.model.userinfo.system.SystemData
import data.network.firefly.OAuthService
import data.network.firefly.SystemInfoService
import io.ktor.client.network.sockets.ConnectTimeoutException

class SystemInfoRepository(
    private val systemInfoService: SystemInfoService,
    private val oAuthService: OAuthService,
    private val appPref: AppPref,
    private val activeUser: UserInfo,
) {

    @Throws(Exception::class)
    suspend fun getCurrentUserInfo(
        userInfo: UserInfo,
        userInfoDao: UserInfoDao,
    ) {
        val userAttribute =
            systemInfoService.getCurrentUserInfo().body()?.userData?.userAttributes
        if (userAttribute != null) {
            val temp = userInfo.copy(
                userEmail = userAttribute.email ?: "",
//                id = userAttribute.
            )
            userInfoDao.insert(temp)
            // On single account systems, role will null
            if (userAttribute.role != null) {
                appPref.setUserRole(userAttribute.role)
            }
        } else {
            throw Exception("Failed to fetch data")
        }
    }

    suspend fun getUserSystem(): SystemData? {
        try {
            val systemInfoModel = systemInfoService.getSystemInfo().body()
            val systemData = systemInfoModel?.systemData
            return if (systemData != null) {
//                Log.d("chandu", "repo systemData:$systemData")
                appPref.setSemVersion(systemData.version)
                appPref.setRemoteApiVersion(systemData.api_version)
                appPref.setUserOs(systemData.os)
                systemData
            } else {
//                Log.d("chandu", "repo systemData null")
                null
            }
        } catch (exception: Exception) {
            return null
        }
    }

    suspend fun isConnectedToServer(): Boolean {
        try {
            val response = systemInfoService.getSystemInfo()
            val systemInfoModel = response.body()
            val systemData = systemInfoModel?.systemData
            return if (systemData != null) {
//                Log.d("chandu", "repo systemData:$systemData")
                appPref.setSemVersion(systemData.version)
                appPref.setRemoteApiVersion(systemData.api_version)
                appPref.setUserOs(systemData.os)
                true
            } else {
//                Log.w("chandu", "repo systemData null")
//                Log.w("chandu", "code: ${response.code}")
//                Log.w("chandu", "message: ${response.message}")
                if (response.code == 401) {

                }
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is ConnectTimeoutException) {
                if (activeUser.userEmail.isNotEmpty()) {
                    return false
                }
            }
        }
        return false
    }
}