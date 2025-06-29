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
                appPref.setSemVersion(systemData.version)
                appPref.setRemoteApiVersion(systemData.api_version)
                appPref.setUserOs(systemData.os)
                systemData
            } else {
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
                appPref.setSemVersion(systemData.version)
                appPref.setRemoteApiVersion(systemData.api_version)
                appPref.setUserOs(systemData.os)
                true
            } else {
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