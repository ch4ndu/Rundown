package app.viewmodel.mock

import androidx.lifecycle.viewModelScope
import app.viewmodel.AccountsViewModel
import data.AppPref
import data.database.model.accounts.AccountAttributes
import data.database.model.accounts.AccountData
import di.DispatcherProvider
import domain.repository.AccountRepository
import domain.usecase.SyncWithServerUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn

class MockAccountsViewModel(
    private val accountRepository: AccountRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val syncWithServerUseCase: SyncWithServerUseCase,
    appPref: AppPref
) : AccountsViewModel(accountRepository, dispatcherProvider, syncWithServerUseCase, appPref) {

    override val accountList = flow {
        delay(5_000)
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
    }.flowOn(dispatcherProvider.default)

    override val lastSyncedAt = flow<String> { "Unknown" }
        .flowOn(dispatcherProvider.io)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    override fun refreshData() {

    }
}