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

package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import data.AppPref
import data.database.AppDatabase
import data.database.dao.AccountChartDataDao
import data.database.dao.AccountsDataDao
import data.database.dao.BudgetDao
import data.database.dao.CategoryDao
import data.database.dao.FireFlyTransactionDataDao
import data.database.dao.LinkedChartEntryWithAccountDao
import data.database.dao.TagsDao
import data.database.dao.UserInfoDao
import data.database.model.accounts.UserInfo
import data.network.firefly.AccountsService
import data.network.firefly.BudgetService
import data.network.firefly.ChartsService
import data.network.firefly.InsightService
import data.network.firefly.OAuthService
import data.network.firefly.SystemInfoService
import data.network.firefly.createAccountsService
import data.network.firefly.createBudgetService
import data.network.firefly.createChartsService
import data.network.firefly.createInsightService
import data.network.firefly.createOAuthService
import data.network.firefly.createSystemInfoService
import data.network.firefly.createTransactionService
import data.network.getKtorFit
import de.jensklingenberg.ktorfit.Ktorfit
import domain.repository.AccountRepository
import domain.repository.AuthRepository
import domain.repository.BudgetRepository
import domain.repository.ChartsRepository
import domain.repository.ConnectivityStateManager
import domain.repository.HealthCheckRepository
import domain.repository.InsightRepository
import domain.repository.SystemInfoRepository
import domain.repository.TransactionRepository
import domain.repository.UserRepository
import domain.usecase.BudgetUseCase
import domain.usecase.GetAccountSpendingUseCase
import domain.usecase.GetBalanceChartDataUseCase
import domain.usecase.GetCashFlowUseCase
import domain.usecase.GetCategorySpendingUseCase
import domain.usecase.GetOverallSpendingUseCase
import domain.usecase.SyncWithServerUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

expect val platformModule: Module

expect val viewModelModule: Module

expect val mockViewModelModule: Module

val dispatcherProvider = module {
    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
    single<CoroutineScope> {
        val dispatcherProvider: DispatcherProvider = get()
        CoroutineScope(SupervisorJob() + dispatcherProvider.default)
    }
}

val networkModule = module {
    factory<UserInfo> {
        runBlocking {
            get<UserInfoDao>().getCurrentActiveUserInfo(isActive = true) ?: UserInfo(-1)
        }
    }
    single<Ktorfit> {
        val activeUser = get<UserInfo>()
        getKtorFit(baseUrl = activeUser.baseUrl, accessToken = activeUser.accessToken)
    }

    single<OAuthService> { get<Ktorfit>().createOAuthService() }
    single<AccountsService> { get<Ktorfit>().createAccountsService() }
    single<BudgetService> { get<Ktorfit>().createBudgetService() }
    single<InsightService> { get<Ktorfit>().createInsightService() }
    single<SystemInfoService> { get<Ktorfit>().createSystemInfoService() }
    single<ChartsService> { get<Ktorfit>().createChartsService() }
    single { get<Ktorfit>().createTransactionService() }
}

val sharedModule = module {
    factory { SavedStateHandle.createHandle(null, null) }

    single<AppPref> { AppPref(get()) }
    single<ConnectivityStateManager> { ConnectivityStateManager(get(), get(), get(), get(), get()) }

    // begin data modules
    single<UserInfoDao> { get<AppDatabase>().userInfoDao() }
    single<FireFlyTransactionDataDao> { get<AppDatabase>().newTransactionDataDao() }
    single<AccountsDataDao> { get<AppDatabase>().accountDataDao() }
    single<LinkedChartEntryWithAccountDao> { get<AppDatabase>().linkedChartEntryWithAccount() }
    single<AccountChartDataDao> { get<AppDatabase>().accountChartDao() }
    single<TagsDao> { get<AppDatabase>().tagsDao() }
    single<CategoryDao> { get<AppDatabase>().categoryDao() }
    single<BudgetDao> { get<AppDatabase>().budgetDao() }
    // end data modules

    //begin domain modules
//    single<UserRepository> { UserRepository(get<AppDatabase>().userInfoDao()) }
//    single<AuthRepository> { AuthRepository(get(), get(), get()) }
//    single<TransactionRepository> { TransactionRepository(get(), get() ,get(), get(), get())}

    single { InsightRepository(get(), get()) }
    single { AuthRepository(get(), get(), get()) }
    single { HealthCheckRepository(get()) }
    single { BudgetRepository(get(), get(), get(), get()) }

    single { TransactionRepository(get(), get(), get(), get(), get()) }
    single { AccountRepository(get(), get(), get()) }
    single { ChartsRepository(get(), get(), get(), get()) }
    single { SystemInfoRepository(get(), get(), get(), get()) }
    single { UserRepository(get()) }

    single { GetBalanceChartDataUseCase(get(), get()) }
    single { GetAccountSpendingUseCase(get(), get()) }
    single { GetCategorySpendingUseCase(get(), get()) }
    single { BudgetUseCase(get(), get()) }
    single { GetOverallSpendingUseCase(get(), get(), get()) }
    single { GetCashFlowUseCase(get(), get(), get()) }
    single { SyncWithServerUseCase(get(), get(), get(), get(), get(), get()) }
    //end domain modules
}

val allModules = listOf(dispatcherProvider, sharedModule, platformModule, networkModule, viewModelModule)