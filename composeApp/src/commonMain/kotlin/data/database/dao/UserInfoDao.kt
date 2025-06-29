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

package data.database.dao

import androidx.room.Dao
import androidx.room.Query
import data.database.model.accounts.UserInfo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserInfoDao : BaseDao<UserInfo> {

    @Query("SELECT * FROM userInfo ORDER BY activeUser DESC")
    abstract suspend fun getAllUsers(): List<UserInfo>

    @Query("SELECT * FROM userInfo WHERE activeUser =:isActive")
    abstract suspend fun getCurrentActiveUserInfo(isActive: Boolean = true): UserInfo?

    @Query("SELECT * FROM userInfo WHERE activeUser =:isActive")
    abstract fun getCurrentActiveUserInfoFlow(isActive: Boolean = true): Flow<UserInfo?>

    @Query("SELECT userEmail FROM userInfo WHERE activeUser =:isActive")
    abstract suspend fun getCurrentActiveUserEmail(isActive: Boolean = true): String

    @Query("SELECT baseUrl FROM userInfo WHERE activeUser =:isActive")
    abstract suspend fun getCurrentActiveUserUrl(isActive: Boolean = true): String

//    @Query("UPDATE userInfo SET activeUser =:activeUser WHERE id =:userId")
//    abstract suspend fun updateActiveUser(userId: Long, activeUser: Boolean = true)

    @Query("DELETE FROM userInfo WHERE activeUser =:isActive")
    abstract suspend fun deleteCurrentUser(isActive: Boolean = true)
}