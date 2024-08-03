@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package app.viewmodel

import androidx.lifecycle.SavedStateHandle
import data.database.dao.FireFlyTransactionDataDao
import di.DispatcherProvider
import domain.atBeginningOfMonth
import domain.model.ExpenseIncomeData
import domain.repository.TransactionRepository
import domain.usecase.GetCategorySpendingUseCase
import domain.withEndOfMonthAtEndOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class CategoryDetailsViewModel(
    private val transactionRepository: TransactionRepository,
    getCategorySpendingUseCase: GetCategorySpendingUseCase,
    dispatcherProvider: DispatcherProvider,
    fireFlyTransactionDataDao: FireFlyTransactionDataDao,
    savedStateHandle: SavedStateHandle
) : CategoriesOverviewViewModel(
    transactionRepository, getCategorySpendingUseCase, dispatcherProvider, fireFlyTransactionDataDao
) {

    private val spendingDateSelected = MutableStateFlow<ExpenseIncomeData?>(null)

    //    val selectedCategory = savedStateHandle.getStateFlow("category", "")
    val selectedCategory = MutableStateFlow("")

    fun setSelectedCategory(category: String) {
        selectedCategory.value = category
    }

    val categorySpending = selectedCategory.filterNot { it.isEmpty() }.flatMapLatest { category ->
        allCategoriesSpending.flatMapLatest { categorySpendingList ->
            flowOf(
                categorySpendingList.find { it.categoryName == category }
            )
        }
    }.distinctUntilChanged()
        .flowOn(dispatcherProvider.io)

    val transactionsForCategory =
        spendingDateSelected.filterNotNull().flatMapLatest { spendingData ->
            val startDate = spendingData.date.atBeginningOfMonth()
            val endDate = startDate.withEndOfMonthAtEndOfDay()
            val categoryName = selectedCategory.first()
            transactionRepository.getTransactionsForCategory(
                startDate,
                endDate,
                categoryName
            )
        }.flowOn(dispatcherProvider.io)

    fun updateSelectedSpendingData(expenseIncomeData: ExpenseIncomeData) {
        spendingDateSelected.value = expenseIncomeData
    }
}