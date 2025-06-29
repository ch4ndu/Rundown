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

package app.viewmodel.mock

import app.viewmodel.AccountsViewModel
import data.AppPref
import data.database.model.accounts.AccountAttributes
import data.database.model.accounts.AccountData
import data.database.serializers.DateSerializer
import di.DispatcherProvider
import domain.currentDate
import domain.repository.AccountRepository
import domain.usecase.SyncWithServerUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.format

class MockAccountsViewModel(
    accountRepository: AccountRepository,
    dispatcherProvider: DispatcherProvider,
    syncWithServerUseCase: SyncWithServerUseCase,
    appPref: AppPref
) : AccountsViewModel(accountRepository, dispatcherProvider, syncWithServerUseCase, appPref) {

    override val accountList = flow {
        emit(
            listOf(
                AccountData(
                    id = 0, attributes = AccountAttributes(
                        account_number = "10",
                        name = "Checking",
                        type = "asset",
                        current_balance = 500.0
                    )
                ),
                AccountData(
                    id = 1, attributes = AccountAttributes(
                        account_number = "11",
                        name = "Savings",
                        type = "asset",
                        current_balance = 6000.0
                    )
                ),
                AccountData(
                    id = 2, attributes = AccountAttributes(
                        account_number = "12",
                        name = "CreditCard1",
                        type = "asset",
                        current_balance = 500.0
                    )
                ),
                AccountData(
                    id = 3, attributes = AccountAttributes(
                        account_number = "13",
                        name = "CreditCard2",
                        type = "asset",
                        current_balance = -500.0
                    )
                ),
                AccountData(
                    id = 4, attributes = AccountAttributes(
                        account_number = "14",
                        name = "CreditCard3",
                        type = "asset",
                        current_balance = 0.0
                    )
                ),
                AccountData(
                    id = 5, attributes = AccountAttributes(
                        account_number = "15",
                        name = "CreditCard4",
                        type = "asset",
                        current_balance = -1500.0
                    )
                )
            )
        )
    }.flowOn(dispatcherProvider.default).toStateFlow(initial = emptyList())

    override val lastSyncedAt =
        flow { emit(currentDate().format(DateSerializer.displayFormat)) }
        .flowOn(dispatcherProvider.io)
            .toStateFlow(initial = "")

    override fun refreshData() {

    }
}