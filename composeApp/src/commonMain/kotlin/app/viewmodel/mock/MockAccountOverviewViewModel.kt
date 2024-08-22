package app.viewmodel.mock

import androidx.lifecycle.SavedStateHandle
import app.model.TotalsSummary
import app.viewmodel.AccountOverviewViewModel
import data.database.dao.FireFlyTransactionDataDao
import data.database.model.transaction.FireFlyTransaction
import di.DispatcherProvider
import domain.currentDate
import domain.repository.TransactionRepository
import domain.usecase.SyncWithServerUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime

class MockAccountOverviewViewModel(
    private val transactionRepository: TransactionRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val fireFlyTransactionDataDao: FireFlyTransactionDataDao,
    private val syncWithServerUseCase: SyncWithServerUseCase,
    savedStateHandle: SavedStateHandle
) : AccountOverviewViewModel(
    transactionRepository,
    dispatcherProvider,
    fireFlyTransactionDataDao,
    syncWithServerUseCase,
    savedStateHandle
) {

    override val topTransactionsFlow = flow {
        emit(
            listOf(
                FireFlyTransaction(
                    description = "sample description",
                    transactionType = "expense",
                    destination_type = "destination",
                    destination_name = "Checking Account",
                    category_name = "coffee",
                    amount = 10.12,
                    transaction_journal_id = 1L,
                    destination_id = 1L,
                    currency_name = "",
                    currency_id = 1L,
                    currency_decimal_places = 1,
                    currency_symbol = "$",
                    date = currentDate(),
                    internal_reference = "sdf",
                    budget_id = null,
                    isPending = false,
                    source_id = null,
                    tags = emptyList(),
                    user = 1,
                    currency_code = "",
                    budget_name = null,
                    category_id = null,
                    source_iban = null,
                    source_name = null,
                    source_type = null,
                    notes = null,
                    order = 1
                ),
                FireFlyTransaction(
                    description = "sample description",
                    transactionType = "transfer",
                    destination_type = "destination",
                    destination_name = "Credit1",
                    category_name = "category",
                    amount = 50.0,
                    transaction_journal_id = 2L,
                    destination_id = 1L,
                    currency_name = "",
                    currency_id = 1L,
                    currency_decimal_places = 1,
                    currency_symbol = "$",
                    date = currentDate(),
                    internal_reference = "sdf",
                    budget_id = null,
                    isPending = false,
                    source_id = null,
                    tags = emptyList(),
                    user = 1,
                    currency_code = "",
                    budget_name = null,
                    category_id = null,
                    source_iban = null,
                    source_name = null,
                    source_type = null,
                    notes = null,
                    order = 1
                ),
                FireFlyTransaction(
                    description = "sample description",
                    transactionType = "expense",
                    destination_type = "Loooooong destination",
                    destination_name = "Savings",
                    category_name = "category",
                    amount = 10.12,
                    transaction_journal_id = 3L,
                    destination_id = 1L,
                    currency_name = "",
                    currency_id = 1L,
                    currency_decimal_places = 1,
                    currency_symbol = "$",
                    date = currentDate(),
                    internal_reference = "sdf",
                    budget_id = null,
                    isPending = false,
                    source_id = null,
                    tags = emptyList(),
                    user = 1,
                    currency_code = "",
                    budget_name = null,
                    category_id = null,
                    source_iban = null,
                    source_name = null,
                    source_type = null,
                    notes = null,
                    order = 1
                ),
                FireFlyTransaction(
                    description = "Comcast",
                    transactionType = "expense",
                    destination_type = "asset",
                    destination_name = "Checking",
                    category_name = "HomeExpense",
                    amount = 10.12,
                    transaction_journal_id = 4L,
                    destination_id = 1L,
                    currency_name = "",
                    currency_id = 1L,
                    currency_decimal_places = 1,
                    currency_symbol = "$",
                    date = currentDate(),
                    internal_reference = "sdf",
                    budget_id = null,
                    isPending = false,
                    source_id = null,
                    tags = emptyList(),
                    user = 1,
                    currency_code = "",
                    budget_name = null,
                    category_id = null,
                    source_iban = null,
                    source_name = null,
                    source_type = null,
                    notes = null,
                    order = 1
                ),
                FireFlyTransaction(
                    description = "Rent Payment",
                    transactionType = "expense",
                    destination_type = "asset",
                    destination_name = "Checking",
                    category_name = "HomeExpense",
                    amount = 600.0,
                    transaction_journal_id = 5L,
                    destination_id = 1L,
                    currency_name = "",
                    currency_id = 1L,
                    currency_decimal_places = 1,
                    currency_symbol = "$",
                    date = currentDate(),
                    internal_reference = "sdf",
                    budget_id = null,
                    isPending = false,
                    source_id = null,
                    tags = emptyList(),
                    user = 1,
                    currency_code = "",
                    budget_name = null,
                    category_id = null,
                    source_iban = null,
                    source_name = null,
                    source_type = null,
                    notes = null,
                    order = 1
                ),
                FireFlyTransaction(
                    description = "Chevron",
                    transactionType = "expense",
                    destination_type = "asset",
                    destination_name = "Credit",
                    category_name = "Gas",
                    amount = 45.54,
                    transaction_journal_id = 6L,
                    destination_id = 1L,
                    currency_name = "",
                    currency_id = 1L,
                    currency_decimal_places = 1,
                    currency_symbol = "$",
                    date = currentDate(),
                    internal_reference = "sdf",
                    budget_id = null,
                    isPending = false,
                    source_id = null,
                    tags = emptyList(),
                    user = 1,
                    currency_code = "",
                    budget_name = null,
                    category_id = null,
                    source_iban = null,
                    source_name = null,
                    source_type = null,
                    notes = null,
                    order = 1
                ),
                FireFlyTransaction(
                    description = "Whole Foods",
                    transactionType = "expense",
                    destination_type = "asset",
                    destination_name = "CreditCard",
                    category_name = "Groceries",
                    amount = 87.54,
                    transaction_journal_id = 7L,
                    destination_id = 1L,
                    currency_name = "",
                    currency_id = 1L,
                    currency_decimal_places = 1,
                    currency_symbol = "$",
                    date = currentDate(),
                    internal_reference = "sdf",
                    budget_id = null,
                    isPending = false,
                    source_id = null,
                    tags = emptyList(),
                    user = 1,
                    currency_code = "",
                    budget_name = null,
                    category_id = null,
                    source_iban = null,
                    source_name = null,
                    source_type = null,
                    notes = null,
                    order = 1
                )
            )
        )
    }

    override fun refreshRemoteData() {
        //Do Nothing
    }

    override val tagsForAccountWithDate: Flow<List<String>> = flow {
        emit(
            listOf("Groceries", "HomeExpenses", "Coffee", "Misc")
        )
    }

    override val tagSummaryAccount: Flow<HashMap<String, TotalsSummary>> = flow {
        emit(
            hashMapOf(
                Pair(
                    "Groceries",
                    TotalsSummary(expenseSum = 300.0, transferSum = 0.0, incomeSum = 0.0)
                ),
                Pair(
                    "HomeExpenses",
                    TotalsSummary(expenseSum = 1200.0, transferSum = 0.0, incomeSum = 0.0)
                ),
                Pair(
                    "Coffee",
                    TotalsSummary(expenseSum = 300.0, transferSum = 0.0, incomeSum = 0.0)
                ),
                Pair(
                    "Misc",
                    TotalsSummary(expenseSum = 100.0, transferSum = 200.0, incomeSum = 0.0)
                )
            )
        )
    }

    override val transactionsForActiveTag: Flow<List<FireFlyTransaction>> = topTransactionsFlow
    override val categoriesForAccountWithDate: Flow<List<String>> = tagsForAccountWithDate
    override val categorySummaryAccount: Flow<HashMap<String, TotalsSummary>> = tagSummaryAccount
    override val transactionsForActiveCategory: Flow<List<FireFlyTransaction>> =
        transactionsForActiveTag

    override fun getNewTransactionListForAccount(
        accountType: String,
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<FireFlyTransaction>> {
        return transactionsForActiveTag
    }
}