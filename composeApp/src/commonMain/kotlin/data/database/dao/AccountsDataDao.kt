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

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import data.database.model.accounts.AccountData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AccountsDataDao : BaseDao<AccountData> {

    @Query("SELECT * FROM accounts WHERE name =:accountName AND type =:accountType")
    abstract suspend fun getAccountByNameAndType(
        accountName: String,
        accountType: String
    ): AccountData

    @Query("SELECT * FROM accounts WHERE type =:accountType ORDER BY id ASC")
    abstract fun getAccountsByType(accountType: String): Flow<List<AccountData>>

    @Query("SELECT * FROM accounts WHERE type =:accountType ORDER BY id ASC")
    abstract fun getAccountsByTypePaging(accountType: String): PagingSource<Int, AccountData>

    @Query("SELECT * FROM accounts WHERE type =:accountType ORDER BY id ASC")
    abstract fun getAccountsFlowByType(accountType: String): Flow<List<AccountData>>

    @Query("DELETE FROM accounts WHERE type =:accountType AND isPending IS NOT :isPending")
    abstract suspend fun deleteAccountByType(
        accountType: String,
        isPending: Boolean = true
    ): Int

    @Query("SELECT * FROM accounts WHERE name =:name")
    abstract fun getAccountFlowForName(name: String): Flow<AccountData?>
}