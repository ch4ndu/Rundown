package app.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import data.AppPref
import di.DispatcherProvider
import domain.repository.AccountRepository
import domain.usecase.SyncWithServerUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.lighthousegames.logging.logging

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

//    val accountListPaging = accountType.flatMapLatest { accountType ->
//        flowOf(
//            Pager(
//                PagingConfig(
//                    pageSize = Constants.PAGE_SIZE,
//                    enablePlaceholders = false,
//                ),
////            remoteMediator = accountRepository.loadNetworkData(accountType)
//                remoteMediator = null
//            ) {
//                accountRepository.getAccountListPaging(accountType)
//            }
//        )
//    }

    open val lastSyncedAt = appPref.lastSyncedAt()
        .flowOn(dispatcherProvider.io)
        .toStateFlow(initial = "")

    private val log = logging()

    open fun refreshData() {
        log.d { "refreshRemoteData" }
        viewModelScope.launch(dispatcherProvider.default) {
            syncWithServerUseCase.invoke()
        }
    }
}