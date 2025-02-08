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
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.udnahc.firefly.DailySyncWorker

actual val platformModule = module {
    single<AppDatabase> {
        getRoomDatabase(androidContext())
    }
    single { PreferenceStore(androidContext()).getDataStore() }

    worker { DailySyncWorker(get(), get(), get(), get(), get()) }
}

val serverViewModels = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::AccountsViewModel)
    viewModelOf(::SyncWithServerViewModel)
    viewModelOf(::AccountOverviewViewModel)
    viewModelOf(::AccountChartsViewModel)
    viewModelOf(::AccountCashFlowDetailsViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::DailySpendingDetailsViewModel)
    viewModelOf(::BudgetListOverviewViewModel)
    viewModelOf(::CategoriesOverviewViewModel)
    viewModelOf(::CategoryDetailsViewModel)
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