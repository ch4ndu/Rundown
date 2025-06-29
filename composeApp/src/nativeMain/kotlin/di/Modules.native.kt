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

import app.viewmodel.AccountCashFlowDetailsViewModel
import app.viewmodel.AccountChartsViewModel
import app.viewmodel.AccountOverviewViewModel
import app.viewmodel.AccountsViewModel
import app.viewmodel.AuthViewModel
import app.viewmodel.BudgetListOverviewViewModel
import app.viewmodel.CategoriesOverviewViewModel
import app.viewmodel.CategoryDetailsViewModel
import app.viewmodel.DailySpendingDetailsViewModel
import app.viewmodel.HomeViewModel
import app.viewmodel.SyncWithServerViewModel
import app.viewmodel.mock.MockAccountCashFlowDetailsViewModel
import app.viewmodel.mock.MockAccountChartsViewModel
import app.viewmodel.mock.MockAccountOverviewViewModel
import app.viewmodel.mock.MockAccountsViewModel
import app.viewmodel.mock.MockAuthViewModel
import app.viewmodel.mock.MockBudgetListOverviewViewModel
import app.viewmodel.mock.MockCategoriesOverviewViewModel
import app.viewmodel.mock.MockCategoryDetailsViewModel
import app.viewmodel.mock.MockDailySpendingDetailsViewModel
import app.viewmodel.mock.MockHomeViewModel
import app.viewmodel.mock.MockSyncWithServerViewModel
import data.PreferenceStore
import data.database.AppDatabase
import data.database.getRoomDatabase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    single<AppDatabase> {
        getRoomDatabase()
    }
    single { PreferenceStore().getDataStore() }
}

val serverModules = module {
    factory { AuthViewModel(get(), get(), get()) }
    factory { AccountsViewModel(get(), get(), get(), get()) }
    factory { SyncWithServerViewModel(get(), get(), get()) }
    factory { AccountOverviewViewModel(get(), get(), get(), get(), get()) }
    factory { AccountChartsViewModel(get(), get(), get(), get(), get(), get(), get()) }
    factory { AccountCashFlowDetailsViewModel(get(), get(), get(), get()) }
    factory { HomeViewModel(get(), get(), get(), get(), get(), get()) }
    factory { DailySpendingDetailsViewModel(get(), get(), get(), get()) }
    factory { BudgetListOverviewViewModel(get()) }
    factory { CategoriesOverviewViewModel(get(), get(), get(), get()) }
    factory { CategoryDetailsViewModel(get(), get(), get(), get(), get()) }
}

val mockModules = module {
    factoryOf(::MockAuthViewModel).bind<AuthViewModel>()
    factoryOf(::MockAccountsViewModel).bind<AccountsViewModel>()
    factoryOf(::MockSyncWithServerViewModel).bind<SyncWithServerViewModel>()
    factoryOf(::MockAccountOverviewViewModel).bind<AccountOverviewViewModel>()
    factoryOf(::MockAccountChartsViewModel).bind<AccountChartsViewModel>()
    factoryOf(::MockAccountCashFlowDetailsViewModel).bind<AccountCashFlowDetailsViewModel>()
    factoryOf(::MockHomeViewModel).bind<HomeViewModel>()
    factoryOf(::MockDailySpendingDetailsViewModel).bind<DailySpendingDetailsViewModel>()
    factoryOf(::MockBudgetListOverviewViewModel).bind<BudgetListOverviewViewModel>()
    factoryOf(::MockCategoriesOverviewViewModel).bind<CategoriesOverviewViewModel>()
    factoryOf(::MockCategoryDetailsViewModel).bind<CategoryDetailsViewModel>()
}
actual val viewModelModule = serverModules

//actual val viewModelModule = serverModules
actual val mockViewModelModule = mockModules