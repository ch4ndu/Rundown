package data.database.dao

import Constants
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import data.database.model.transaction.FireFlyTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
abstract class FireFlyTransactionDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    abstract suspend fun insert(vararg obj: FireFlyTransaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    abstract suspend fun insertAll(fireFlyTransactions: List<FireFlyTransaction>)

    @Query("SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE transactionType = :type")
    abstract suspend fun getTransactionList(type: String): MutableList<FireFlyTransaction>

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate)" +
                " AND transactionType in(:types)"
    )
    abstract suspend fun getTransactionSumByType(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        types: List<String>
    ): Double

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "tags  LIKE '%' || :tag || '%'"
    )
    abstract fun getTransactionsByTag(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        tag: String
    ): Flow<List<FireFlyTransaction>>

    @Query(
        "SELECT tags FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate)"
    )
    abstract fun getTagsByDate(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Flow<List<String>>

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) ORDER BY amount DESC LIMIT :limit"
    )
    abstract fun getTopTransactionListWithDateFlow(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        limit: Int
    ): Flow<List<FireFlyTransaction>>

    @Transaction
    @Query(
        "DELETE FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate)"
    )
    abstract suspend fun deleteTransactions(
        startDate: Long,
        endDate: Long
    ): Int

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "tags  LIKE '%' || :tag || '%'"
    )
    abstract fun getTransactionsFromAccountByTag(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long,
        tag: String
    ): Flow<List<FireFlyTransaction>>

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "transactionType in (:types) AND " +
                "tags  LIKE '%' || :tag || '%'"
    )
    abstract fun getTransactionsFromAccountByTagAndType(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long,
        types: List<String>,
        tag: String
    ): Flow<List<FireFlyTransaction>>

    @Query(
        "SELECT tags FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId)"
    )
    abstract fun getTagsForAccountByDate(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long
    ): Flow<List<String>>

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "tags  LIKE '%' || :tag || '%'"
    )
    abstract suspend fun getTransactionSumByAccountAndTag(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        tag: String,
        accountId: Long
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "transactionType in (:types) AND " +
                "tags  LIKE '%' || :tag || '%'"
    )
    abstract suspend fun getTransactionSumByAccountAndTagAndType(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        tag: String,
        types: List<String>,
        accountId: Long
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "category_name  =:category"
    )
    abstract suspend fun getTransactionSumByAccountAndCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String,
        accountId: Long
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "transactionType in (:types) AND " +
                "category_name  =:category"
    )
    abstract suspend fun getTransactionSumByAccountAndCategoryAndType(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String,
        types: List<String>,
        accountId: Long
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId) AND " +
                "transactionType IN (:expenseCategoryTypeList) AND " +
                "category_name  =:category"
    )
    abstract suspend fun getExpenseSumByAccountAndCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String,
        accountId: Long,
        expenseCategoryTypeList: List<String>
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId) AND " +
                "transactionType IN (:incomeCategoryTypeList) AND " +
                "category_name  =:category"
    )
    abstract suspend fun getIncomeSumByAccountAndCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String,
        accountId: Long,
        incomeCategoryTypeList: List<String>
    ): Double

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "category_name  =:category"
    )
    abstract fun getTransactionsFromAccountByCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long,
        category: String
    ): Flow<List<FireFlyTransaction>>

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "transactionType IN (:expenseTypeList) AND " +
                "category_name  =:category"
    )
    abstract fun getTransactionsFromAccountByCategoryAndType(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long,
        category: String,
        expenseTypeList: List<String>,
    ): Flow<List<FireFlyTransaction>>

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "source_name = :accountName"
    )
    abstract suspend fun getTransactionListBySourceName(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountName: String
    ): MutableList<FireFlyTransaction>

    @Transaction
    @Query(
        "DELETE FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId)"
    )
    abstract suspend fun deleteTransactionsForAccount(
        startDate: Long,
        endDate: Long,
        accountId: Long
    ): Int

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "(date BETWEEN :startDate AND :endDate) ORDER BY amount DESC LIMIT :limit"
    )
    abstract fun getTopTransactionListForAccountWithDateFlow(
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        limit: Int
    ): Flow<MutableList<FireFlyTransaction>>

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "(date BETWEEN :startDate AND :endDate) ORDER BY date DESC"
    )
    abstract fun getTransactionBySourceIdAndDate(
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<FireFlyTransaction>>

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(destination_id =:accountId OR destination_id =:accountId) AND " +
                "(date BETWEEN :startDate AND :endDate) ORDER BY date ASC"
    )
    abstract fun getTransactionByDestinationIdAndDate(
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<FireFlyTransaction>>

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(source_id IN (:accountIdList)) AND " +
                "(transactionType in (:types)) AND " +
                "(date BETWEEN :startDate AND :endDate) ORDER BY date ASC"
    )
    abstract fun getExpensesForAccounts(
        accountIdList: List<Long>,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        types: List<String> = Constants.NEW_EXPENSE_TRANSACTION_TYPES
    ): Flow<List<FireFlyTransaction>>

    //Budgets
    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(budget_id =:budgetId)"
    )
    abstract suspend fun getTransactionSumByBudget(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        budgetId: Long
    ): Double

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId) AND " +
                "(budget_id =:budgetId) ORDER BY date DESC"
    )
    abstract fun getTransactionsFromAccountByBudget(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long,
        budgetId: Long
    ): Flow<List<FireFlyTransaction>>

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "budget_id  =:budgetId AND " +
                "transactionType in (:types)"
    )
    abstract suspend fun getExpenseSumByBudget(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        budgetId: Long,
        types: List<String> = Constants.NEW_EXPENSE_TRANSACTION_TYPES
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "budget_id  =:budgetId AND " +
                "transactionType in (:types)"
    )
    abstract suspend fun getIncomeSumByBudget(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        budgetId: Long,
        types: List<String> = Constants.NEW_INCOME_TRANSACTION_TYPES
    ): Double


    //Categories
    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(category_name =:category) ORDER BY date DESC"
    )
    abstract fun getTransactionsForCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String
    ): Flow<List<FireFlyTransaction>>

    @Query(
        "SELECT category_name FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "(source_id =:accountId OR destination_id =:accountId)"
    )
    abstract fun getCategoriesForAccountByDate(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long
    ): Flow<List<String>>

    @Query(
        "SELECT category_name FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate)"
    )
    abstract fun getAllCategoriesByDate(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<String>>

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "category_name  =:category"
    )
    abstract suspend fun getTransactionSumByCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String,
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "category_name  =:category AND " +
                "transactionType in (:types)"
    )
    abstract suspend fun getExpenseSumByCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String,
        types: List<String> = Constants.NEW_EXPENSE_TRANSACTION_TYPES
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "category_name  =:category AND " +
                "transactionType in (:types)"
    )
    abstract suspend fun getIncomeSumByCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String,
        types: List<String> = Constants.NEW_INCOME_TRANSACTION_TYPES
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "source_id in (:accountIdList) AND " +
                "transactionType in (:types)"
    )
    abstract suspend fun getExpenseSumForRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountIdList: List<Long>,
        types: List<String> = Constants.NEW_EXPENSE_TRANSACTION_TYPES
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "destination_id in (:accountIdList) AND " +
                "transactionType in (:types)"
    )
    abstract suspend fun getIncomeSumForRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountIdList: List<Long>,
        types: List<String> = Constants.NEW_INCOME_TRANSACTION_TYPES
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "source_id =:accountId AND " +
                "transactionType in (:types)"
    )
    abstract suspend fun getTransferSumForSourceWithRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long,
        types: List<String> = Constants.TRANSFER_TRANSACTION_TYPES
    ): Double

    @Query(
        "SELECT sum(amount) FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "destination_id =:accountId AND " +
                "transactionType in (:types)"
    )
    abstract suspend fun getTransferSumForDestinationWithRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long,
        types: List<String> = Constants.TRANSFER_TRANSACTION_TYPES
    ): Double

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "((source_id in (:accountIdList) AND transactionType in (:expenseTypes)) OR " +
                "(source_id in (:accountIdList) AND transactionType in (:transferTypes))) " +
                "ORDER BY date DESC"
    )
    abstract suspend fun getCashFlowExpensesForAccount(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountIdList: List<Long>,
        expenseTypes: List<String> = Constants.NEW_EXPENSE_TRANSACTION_TYPES,
        transferTypes: List<String> = Constants.TRANSFER_TRANSACTION_TYPES
    ): List<FireFlyTransaction>

    @Query(
        "SELECT * FROM ${FireFlyTransaction.TABLE_NAME} WHERE " +
                "(date BETWEEN :startDate AND :endDate) AND " +
                "((destination_id in (:accountIdList) AND transactionType in (:incomeTypes)) OR " +
                "(destination_id in (:accountIdList) AND transactionType in (:transferTypes))) " +
                "ORDER BY date DESC"
    )
    abstract suspend fun getCashFlowIncomesForAccount(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountIdList: List<Long>,
        incomeTypes: List<String> = Constants.NEW_INCOME_TRANSACTION_TYPES,
        transferTypes: List<String> = Constants.TRANSFER_TRANSACTION_TYPES
    ): List<FireFlyTransaction>
}