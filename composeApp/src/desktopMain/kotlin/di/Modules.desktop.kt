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
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {

    single<AppDatabase> {
        getRoomDatabase()
    }
    single { PreferenceStore().getDataStore() }
}
actual val viewModelModule = module {
    single { AuthViewModel(get(), get(), get()) }
    single { AccountsViewModel(get(), get(), get(), get()) }
    single { SyncWithServerViewModel(get(), get(), get()) }
    single { AccountOverviewViewModel(get(), get(), get(), get(), get()) }
    single { AccountChartsViewModel(get(), get(), get(), get(), get(), get(), get()) }
    single { AccountCashFlowDetailsViewModel(get(), get(), get(), get()) }
    single { HomeViewModel(get(), get(), get(), get(), get(), get()) }
    single { DailySpendingDetailsViewModel(get(), get(), get(), get()) }
    single { BudgetListOverviewViewModel(get()) }
    single { CategoriesOverviewViewModel(get(), get(), get(), get()) }
    single { CategoryDetailsViewModel(get(), get(), get(), get(), get()) }

}
actual val mockViewModelModule = module {
    single { MockAuthViewModel(get(), get(), get()) }.bind<AuthViewModel>()
    single { MockAccountsViewModel(get(), get(), get(), get()) }.bind<AccountsViewModel>()
    single {
        MockAccountOverviewViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }.bind<AccountOverviewViewModel>()
    singleOf(::MockSyncWithServerViewModel) { bind<SyncWithServerViewModel>() }
    singleOf(::MockAccountChartsViewModel) { bind<AccountChartsViewModel>() }
    singleOf(::MockAccountCashFlowDetailsViewModel) { bind<AccountCashFlowDetailsViewModel>() }
    singleOf(::MockHomeViewModel) { bind<HomeViewModel>() }
    singleOf(::MockDailySpendingDetailsViewModel) { bind<DailySpendingDetailsViewModel>() }
    singleOf(::MockBudgetListOverviewViewModel) { bind<BudgetListOverviewViewModel>() }
    singleOf(::MockCategoriesOverviewViewModel) { bind<CategoriesOverviewViewModel>() }
    singleOf(::MockCategoryDetailsViewModel) { bind<CategoryDetailsViewModel>() }
}