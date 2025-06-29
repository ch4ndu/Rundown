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

package domain.usecase

import data.database.model.accounts.AccountData
import di.DispatcherProvider
import domain.model.DateRange
import domain.model.GroupedExpenseData
import domain.repository.ChartsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetBalanceChartDataUseCase(
    private val chartsRepository: ChartsRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    fun accountChartData(
        dateRange: DateRange,
        accountName: String,
    ): Flow<GroupedExpenseData?> {
        return chartsRepository.loadLocalChartDataForAccountFlow(
            dateRange.startDate,
            dateRange.endDate,
            accountName
        ).flowOn(dispatcherProvider.io)
    }

    suspend fun getCombinedChartData(
        dateRange: DateRange,
        accountName: List<AccountData>
    ): Flow<GroupedExpenseData?> {
        return chartsRepository.loadLocalCombinedChartDataFlow(
            startDate = dateRange.startDate,
            endDate = dateRange.endDate,
            accountNameList = accountName
        )
    }
}