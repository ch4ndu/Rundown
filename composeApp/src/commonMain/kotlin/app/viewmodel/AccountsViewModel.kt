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

package app.viewmodel

import Constants
import TARGET
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import data.AppPref
import di.DispatcherProvider
import domain.repository.AccountRepository
import domain.usecase.SyncWithServerUseCase
import getPlatform
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.lighthousegames.logging.logging
import scheduleImmediateSync

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
open class AccountsViewModel(
    private val accountRepository: AccountRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val syncWithServerUseCase: SyncWithServerUseCase,
    appPref: AppPref
) : BaseViewModel() {

    private val accountType = MutableStateFlow("asset")

    open val accountList = accountType.flatMapLatest { accountType ->
        accountRepository.getAccountList(accountType)
    }.toStateFlow(initial = emptyList())

    val accountListPaging = accountType.flatMapLatest { accountType ->
        flowOf(
            Pager(
                PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    enablePlaceholders = false,
                ),
//            remoteMediator = accountRepository.loadNetworkData(accountType)
                remoteMediator = null
            ) {
                accountRepository.getAccountListPaging(accountType)
            }
        )
    }

    open val lastSyncedAt = appPref.lastSyncedAt()
        .flowOn(dispatcherProvider.io)
        .toStateFlow(initial = "")

    private val log = logging()

    open fun refreshData() {
        log.d { "refreshRemoteData" }
        viewModelScope.launch(dispatcherProvider.default) {
            if (getPlatform().target == TARGET.ANDROID) {
                scheduleImmediateSync()
            } else {
                syncWithServerUseCase.invoke()
            }
        }
    }
}