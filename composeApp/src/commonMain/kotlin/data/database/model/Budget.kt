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

package data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import data.enums.BudgetType
import data.database.model.Budget.Companion.TABLE_NAME
import data.database.model.transaction.BudgetNetworkData

@Entity(tableName = TABLE_NAME)
data class Budget(
    @PrimaryKey
    val id: Long,
    val name: String,
    val type: BudgetType,
    val amount: Long?,
    val period: String?,
) {
    companion object {

        const val TABLE_NAME = "budgetTable"
        fun fromBudgetData(budgetNetworkData: BudgetNetworkData): Budget {
            return Budget(
                id = budgetNetworkData.budgetId,
                name = budgetNetworkData.budgetAttributes.name,
                amount = budgetNetworkData.budgetAttributes.auto_budget_amount?.toLong(),
                period = budgetNetworkData.budgetAttributes.auto_budget_period,
                type = BudgetType.fromApi(budgetNetworkData.budgetAttributes.auto_budget_type)
            )
        }
    }
}
