@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package app.viewmodel

import androidx.lifecycle.viewModelScope
import app.ui.areDateRangesEquivalent
import data.database.dao.FireFlyTransactionDataDao
import di.DispatcherProvider
import domain.atBeginningOfMonth
import domain.currentDate
import domain.minusMonths
import domain.model.CategorySpending
import domain.model.DateRange
import domain.repository.TransactionRepository
import domain.usecase.GetCategorySpendingUseCase
import domain.withEndOfMonthAtEndOfDay
import domain.withStartOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

open class CategoriesOverviewViewModel(
    private val transactionRepository: TransactionRepository,
    private val getCategorySpendingUseCase: GetCategorySpendingUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val fireFlyTransactionDataDao: FireFlyTransactionDataDao,
) : BaseViewModel() {

    private val dateRangeFlow =
        MutableStateFlow(
            DateRange(
                startDate = currentDate().minusMonths(11).atBeginningOfMonth().withStartOfDay(),
                endDate = currentDate().withEndOfMonthAtEndOfDay(),
            )
        )

    fun setDateRange(dateRange: DateRange) {
        dateRangeFlow.value = dateRange
    }

    private val categoriesWithDate =
        dateRangeFlow
            .distinctUntilChanged(areDateRangesEquivalent)
            .flatMapLatest { dateRange ->
                fireFlyTransactionDataDao.getAllCategoriesByDate(
                    startDate = dateRange.startDate,
                    endDate = dateRange.endDate
                ).flowOn(dispatcherProvider.io)
                    .distinctUntilChanged()
                    .map { categoryList ->
                        val categorySet = categoryList.toSet()
                        categorySet.toList()
                    }.distinctUntilChanged()
            }

    open val allCategoriesSpending: Flow<List<CategorySpending>> =
        dateRangeFlow.flatMapLatest { range ->
            categoriesWithDate.flatMapLatest { categories ->
                flow {
                    emit(
                        getCategorySpendingUseCase.getCategorySpendingDataForChart(
                            dateRange = range,
                            categoryList = categories,
                            viewModelScope
                        )
                    )
                }
            }
        }.flowOn(dispatcherProvider.default)
}