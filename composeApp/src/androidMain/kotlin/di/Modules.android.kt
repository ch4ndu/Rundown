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
import data.database.AppDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.udnahc.firefly.DailySyncWorker

actual val platformModule = module {
    single<AppDatabase> {
        AppDatabaseBuilder(androidContext()).getDbBuilder().build()
    }
    single { PreferenceStore(androidContext()).getDataStore() }

    worker { DailySyncWorker(get(), get(), get(), get(), get()) }
}

val serverViewModels = module {
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { AccountsViewModel(get(), get(), get(), get()) }
    viewModel { SyncWithServerViewModel(get(), get(), get()) }
    viewModel { AccountOverviewViewModel(get(), get(), get(), get(), get()) }
    viewModel { AccountChartsViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AccountCashFlowDetailsViewModel(get(), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { DailySpendingDetailsViewModel(get(), get(), get(), get()) }
    viewModel { BudgetListOverviewViewModel(get()) }
    viewModel { CategoriesOverviewViewModel(get(), get(), get(), get()) }
    viewModel { CategoryDetailsViewModel(get(), get(), get(), get(), get()) }
}

actual val mockViewModelModule = module {
    viewModelOf(::MockAuthViewModel) { bind<AuthViewModel>() }
    viewModelOf(::MockAccountsViewModel) { bind<AccountsViewModel>() }
    viewModelOf(::MockSyncWithServerViewModel) { bind<SyncWithServerViewModel>() }
    viewModelOf(::MockAccountOverviewViewModel) { bind<AccountOverviewViewModel>() }
    viewModelOf(::MockAccountChartsViewModel) { bind<AccountChartsViewModel>() }
    viewModelOf(::MockAccountCashFlowDetailsViewModel) { bind<AccountCashFlowDetailsViewModel>() }
    viewModelOf(::MockHomeViewModel) { bind<HomeViewModel>() }
    viewModelOf(::MockDailySpendingDetailsViewModel) { bind<DailySpendingDetailsViewModel>() }
    viewModelOf(::MockBudgetListOverviewViewModel) { bind<BudgetListOverviewViewModel>() }
    viewModelOf(::MockCategoriesOverviewViewModel) { bind<CategoriesOverviewViewModel>() }
    viewModelOf(::MockCategoryDetailsViewModel) { bind<CategoryDetailsViewModel>() }
}

actual val viewModelModule = serverViewModels
//actual val viewModelModule = serverViewModels