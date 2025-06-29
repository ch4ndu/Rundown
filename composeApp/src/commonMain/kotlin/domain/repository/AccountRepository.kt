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

import androidx.paging.PagingSource
import data.database.dao.AccountsDataDao
import data.database.model.accounts.AccountData
import data.network.firefly.AccountsService
import di.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.lighthousegames.logging.logging

class AccountRepository(
    private val accountDao: AccountsDataDao,
    private val accountsService: AccountsService,
    private val dispatcherProvider: DispatcherProvider
) {
    private val log = logging()

    fun getAssetAccountListFlow(): Flow<List<AccountData>> {
        return accountDao.getAccountsFlowByType("asset")
    }

    fun getAccountListFlow(accountType: String): Flow<List<AccountData>> {
        return accountDao.getAccountsFlowByType(accountType)
    }

    fun getAccountFlowWithName(name: String): Flow<AccountData?> {
        return accountDao.getAccountFlowForName(name)
    }

    suspend fun getAccountList(accountType: String): Flow<List<AccountData>> {
        return accountDao.getAccountsByType(accountType)
    }

    fun getAccountListPaging(accountType: String): PagingSource<Int, AccountData> {
        return accountDao.getAccountsByTypePaging(accountType)
    }

    suspend fun loadAccountDataFromNetwork(accountType: String) {
        withContext(dispatcherProvider.default) {
            var loadData = true
            var pageKey = 1
            while (loadData) {
                log.d { "loading accountInfo" }
                val networkCall =
                    accountsService.getNewPaginatedAccountType(accountType, pageKey)
                val responseBody = networkCall.body()
                if (networkCall.isSuccessful && responseBody != null) {
                    log.d { "accountInfo call successful" }
                    if (pageKey == 1) {
                        accountDao.deleteAccountByType(accountType)
                    }
                    responseBody.data.forEach { data ->
                        accountDao.insert(data)
                    }
                    if (responseBody.meta.pagination.total_pages !=
                        responseBody.meta.pagination.current_page
                    ) {
                        pageKey += 1
                    } else {
                        loadData = false
                    }
                } else {
                    log.w { "failed to load accountInfo -${networkCall.errorBody()}" }
                    loadData = false
                }
            }
        }
    }
}