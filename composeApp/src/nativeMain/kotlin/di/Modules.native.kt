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
import data.PreferenceStore
import data.database.AppDatabase
import data.database.AppDatabaseBuilder
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

actual val platformModule = module {

    single<AppDatabase> {
        AppDatabaseBuilder().getDbBuilder().build()
    }
    single { PreferenceStore().getDataStore() }
}
actual val viewModelModule = module {
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