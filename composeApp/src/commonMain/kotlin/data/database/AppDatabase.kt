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

public expect object AppDatabaseCtor : RoomDatabaseConstructor<AppDatabase>