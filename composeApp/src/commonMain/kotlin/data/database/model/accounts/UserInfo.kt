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

package data.database.model.accounts

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "userInfo", indices = [Index(value = ["userEmail"], unique = true)])
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = -1L,
    val userEmail: String = "",
    val secretKey: String = "",
    val accessToken: String = "",
    val baseUrl: String = "",
    val clientId: String = "",
    val refreshToken: String = "",
    val tokenExpiry: Long = -1L,
    val authMethod: String = "",
    val activeUser: Boolean = false
)