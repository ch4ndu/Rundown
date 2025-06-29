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

import data.database.dao.UserInfoDao
import data.database.model.accounts.UserInfo
import kotlinx.coroutines.flow.Flow


class UserRepository(private val userInfoDao: UserInfoDao) {

    fun getCurrentActiveUserInfoFlow(isActive: Boolean = true): Flow<UserInfo?> {
        return userInfoDao.getCurrentActiveUserInfoFlow(isActive)
    }

    suspend fun getCurrentActiveUserInfo(isActive: Boolean = true): UserInfo? {
        return userInfoDao.getCurrentActiveUserInfo(isActive)
    }

    suspend fun deleteCurrentUser(isActive: Boolean = true) {
        userInfoDao.deleteCurrentUser(isActive)
    }

    suspend fun insert(userInfo: UserInfo) {
        userInfoDao.insert(userInfo)
    }
}