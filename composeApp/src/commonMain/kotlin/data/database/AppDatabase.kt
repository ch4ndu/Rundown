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

@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import data.database.dao.AccountChartDataDao
import data.database.dao.AccountsDataDao
import data.database.dao.BudgetDao
import data.database.dao.CategoryDao
import data.database.dao.CurrencyDataDao
import data.database.dao.FireFlyTransactionDataDao
import data.database.dao.LinkedChartEntryWithAccountDao
import data.database.dao.TagsDao
import data.database.dao.UserInfoDao
import data.database.model.Budget
import data.database.model.LinkedChartEntryWithAccount
import data.database.model.accounts.AccountData
import data.database.model.accounts.UserInfo
import data.database.model.charts.AccountChartData
import data.database.model.currency.CurrencyData
import data.database.model.transaction.Category
import data.database.model.transaction.FireFlyTransaction
import data.database.model.transaction.Tag


@Database(
    entities = [
        CurrencyData::class,
        AccountData::class,
        FireFlyTransaction::class,
        AccountChartData::class,
        LinkedChartEntryWithAccount::class,
        Tag::class,
        Category::class,
        Budget::class,
        UserInfo::class
    ],
    version = 1, exportSchema = false
)
@TypeConverters(TypeConverterUtil::class)
@ConstructedBy(AppDatabaseCtor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDataDao(): AccountsDataDao
    abstract fun currencyDataDao(): CurrencyDataDao

    abstract fun newTransactionDataDao(): FireFlyTransactionDataDao
    abstract fun accountChartDao(): AccountChartDataDao
    abstract fun linkedChartEntryWithAccount(): LinkedChartEntryWithAccountDao
    abstract fun tagsDao(): TagsDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun userInfoDao(): UserInfoDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
public expect object AppDatabaseCtor : RoomDatabaseConstructor<AppDatabase>