package domain.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import data.database.dao.CategoryDao
import data.database.dao.FireFlyTransactionDataDao
import data.database.dao.TagsDao
import data.database.model.transaction.Category
import data.database.model.transaction.FireFlyTransaction
import data.database.model.transaction.Tag
import data.database.serializers.DateSerializer
import data.network.firefly.TransactionService
import di.DispatcherProvider
import domain.millis
import domain.model.DateRange
import io.ktor.client.network.sockets.ConnectTimeoutException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

class TransactionRepository(
    private val fireFlyTransactionDataDao: FireFlyTransactionDataDao,
    private val transactionService: TransactionService,
    private val dispatcherProvider: DispatcherProvider,
    private val tagsDao: TagsDao,
    private val categoryDao: CategoryDao
) {

    private val TAG = "NewTransactionRepository"

    fun getTransactionByAccountAndDate(
        accountType: String,
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<FireFlyTransaction>> {
        return if (
            accountType.contentEquals("asset") ||
            accountType.contentEquals("revenue")
        ) {
            fireFlyTransactionDataDao.getTransactionBySourceIdAndDate(
                accountId,
                startDate,
                endDate
            )
        } else {
            fireFlyTransactionDataDao.getTransactionByDestinationIdAndDate(
                accountId,
                startDate,
                endDate
            )
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun loadRemoteTransactionsByAccountAndDate(
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ) = object : RemoteMediator<Int, FireFlyTransaction>() {
        private var loadStarted: Boolean = false
        override suspend fun initialize(): InitializeAction {
            return super.initialize()
        }

        override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, FireFlyTransaction>
        ): MediatorResult {
            if (loadStarted) {
                return MediatorResult.Success(true)
            }
            loadStarted = true
            var pageKey = 1
            try {
                when (loadType) {
                    LoadType.REFRESH -> {
                        pageKey = 1
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(true)
                    }

                    LoadType.APPEND -> {
                        pageKey++
                    }
                }
                val numPages = loadAllAccountTransactionsFromNetwork(
                    accountId,
                    startDate,
                    endDate
                )
                return MediatorResult.Success(pageKey == numPages)
            } catch (exception: Exception) {
//                Log.e("TransactionRepository", "loadRemoteTransactionsByAccountAndDate", exception)
                return if (exception.cause is ConnectTimeoutException) {
                    MediatorResult.Success(true)
                } else {
                    MediatorResult.Error(exception)
                }
            }
        }
    }

    fun getTopTransactionsCombinedFlow(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<FireFlyTransaction>> {
//        Log.d(TAG, "getTopTransactionsForAccount")
        return fireFlyTransactionDataDao.getTopTransactionListWithDateFlow(
            startDate = startDate,
            endDate = endDate,
            limit = 10
        ).flowOn(dispatcherProvider.io)
    }

    fun getTopTransactionsForAccountFlow(
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<FireFlyTransaction>> {
//        Log.d(TAG, "getTopTransactionsForAccount")
        return fireFlyTransactionDataDao.getTopTransactionListForAccountWithDateFlow(
            accountId = accountId,
            startDate = startDate,
            endDate = endDate,
            limit = 10
        ).flowOn(dispatcherProvider.io)
    }

    fun getExpensesForAccountListFlow(
        accountIdList: List<Long>,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<FireFlyTransaction>> {
//        Log.d(TAG, "getTopTransactionsForAccount")
        return fireFlyTransactionDataDao.getExpensesForAccounts(
            accountIdList = accountIdList,
            startDate = startDate,
            endDate = endDate
        ).flowOn(dispatcherProvider.io)
    }

    fun getTransactionsForCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String
    ): Flow<List<FireFlyTransaction>> {
        return fireFlyTransactionDataDao.getTransactionsForCategory(
            startDate = startDate,
            endDate = endDate,
            category = category
        ).map {
            it
        }
    }

    suspend fun getExpenseSumByCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String
    ): Double {
        return withContext(dispatcherProvider.io) {
            fireFlyTransactionDataDao.getExpenseSumByCategory(
                startDate = startDate,
                endDate = endDate,
                category = category
            )
        }
    }

    suspend fun getIncomeSumByCategory(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        category: String
    ): Double {
        return withContext(dispatcherProvider.io) {
            fireFlyTransactionDataDao.getIncomeSumByCategory(
                startDate = startDate,
                endDate = endDate,
                category = category
            )
        }
    }

    suspend fun getExpenseSumByBudget(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        budgetId: Long
    ): Double {
        return withContext(dispatcherProvider.io) {
            fireFlyTransactionDataDao.getExpenseSumByBudget(
                startDate = startDate,
                endDate = endDate,
                budgetId = budgetId
            )
        }
    }

    suspend fun getIncomeSumByBudget(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        budgetId: Long
    ): Double {
        return withContext(dispatcherProvider.io) {
            fireFlyTransactionDataDao.getIncomeSumByBudget(
                startDate = startDate,
                endDate = endDate,
                budgetId = budgetId
            )
        }
    }

    suspend fun getExpenseSumForRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountIdList: List<Long>
    ): Double {
        return withContext(dispatcherProvider.io) {
            fireFlyTransactionDataDao.getExpenseSumForRange(
                startDate = startDate,
                endDate = endDate,
                accountIdList = accountIdList
            )
        }
    }

    suspend fun getIncomeSumForRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountIdList: List<Long>,
    ): Double {
        return withContext(dispatcherProvider.io) {
            fireFlyTransactionDataDao.getIncomeSumForRange(
                startDate = startDate,
                endDate = endDate,
                accountIdList = accountIdList
            )
        }
    }

    suspend fun getTransferSumForSourceWithRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long,
    ): Double {
        return withContext(dispatcherProvider.io) {
            fireFlyTransactionDataDao.getTransferSumForSourceWithRange(
                startDate = startDate,
                endDate = endDate,
                accountId = accountId
            )
        }
    }

    suspend fun getTransferSumForDestinationWithRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountId: Long,
    ): Double {
        return withContext(dispatcherProvider.io) {
            fireFlyTransactionDataDao.getTransferSumForDestinationWithRange(
                startDate = startDate,
                endDate = endDate,
                accountId = accountId
            )
        }
    }

    suspend fun getCashFlowExpensesForAccount(
        dateRange: DateRange,
        accountId: Long
    ): List<FireFlyTransaction> {
        return fireFlyTransactionDataDao.getCashFlowExpensesForAccount(
            startDate = dateRange.startDate,
            endDate = dateRange.endDate,
            accountIdList = listOf(accountId),
        )
    }

    suspend fun getCashFlowIncomesForAccount(
        dateRange: DateRange,
        accountId: Long
    ): List<FireFlyTransaction> {
        return fireFlyTransactionDataDao.getCashFlowIncomesForAccount(
            startDate = dateRange.startDate,
            endDate = dateRange.endDate,
            accountIdList = listOf(accountId),
        )
    }

    suspend fun loadAllAccountTransactionsFromNetwork(
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Int {
        return withContext(dispatcherProvider.default) {
            var loadData = true
            var pageKey = 1
            val tagSet = HashSet<Tag>()
            val categorySet = HashSet<Category>()
            val fireFlyTransactionList = mutableListOf<FireFlyTransaction>()
            while (loadData) {
                val networkCall = transactionService.getNewTransactionsByAccountId(
                    accountId,
                    pageKey,
                    startDate.format(DateSerializer.sendToApiFormat),
                    endDate.format(DateSerializer.sendToApiFormat),
                )
                val networkData = networkCall.body()
                if (networkCall.isSuccessful && networkData != null) {
//                networkCall.onSuccess { networkData ->
                    val transactionDataList = networkData.data
                    transactionDataList.forEach { data ->
                        val transactions = data.transactionAttributes.transactions.map {
                            if (it.category_name?.isEmpty() == true) {
                                it.copy(category_name = "Uncategorized")
                            } else {
                                it
                            }
                        }
                        fireFlyTransactionList.addAll(transactions)
                        for (transaction in data.transactionAttributes.transactions) {
                            tagSet.addAll(transaction.getTagList())
                            transaction.category_name?.let {
                                categorySet.add(Category(it))
                            }
                        }
                    }
                    if (
                        networkData.meta.pagination.total_pages !=
                        networkData.meta.pagination.current_page
                    ) {
                        pageKey += 1
                    } else {
                        loadData = false
                    }
                } else {
//                    .onFailure {
//                    Log.w("chandu", "failed to load transactions", it)
                    loadData = false
                }
            }
            if (fireFlyTransactionList.isNotEmpty()) {
                fireFlyTransactionDataDao.deleteTransactionsForAccount(
                    startDate.millis(),
                    endDate.millis(),
                    accountId
                )
            }
            fireFlyTransactionDataDao.insertAll(
                fireFlyTransactionList.filter { !it.isPending }
            )
            tagsDao.insertAll(tagSet.toList())
            categoryDao.insertAll(categorySet.toList())
            pageKey -= 1
            pageKey
        }
    }

    suspend fun loadAllTransactionsFromNetwork(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        scope: CoroutineScope
    ) {
        scope.async(dispatcherProvider.default) {
            var loadData = true
            var pageKey = 1
            while (loadData) {
                val networkCall = transactionService.getAllTransactions(
                    pageKey,
                    startDate.format(DateSerializer.sendToApiFormat),
                    endDate.format(DateSerializer.sendToApiFormat),
                )
                val networkData = networkCall.body()
                if (networkCall.isSuccessful && networkData != null) {
//                networkCall.onSuccess {
                    val transactionList = networkData.data
                    if (transactionList.isNotEmpty()) {
                        val firstTransaction = transactionList.first()
                        val lastTransaction = transactionList.last()
                        fireFlyTransactionDataDao.deleteTransactions(
                            firstTransaction.transactionAttributes.transactions[0].date.millis(),
                            lastTransaction.transactionAttributes.transactions[0].date.millis()
                        )
                    }
                    val tagSet = HashSet<Tag>()
                    val categorySet = HashSet<Category>()
                    transactionList.forEach { data ->
                        val transactions =
                            data.transactionAttributes.transactions.map { transaction ->
                                if (transaction.category_name?.isEmpty() == true) {
                                    transaction.copy(category_name = "Uncategorized")
                                } else {
                                    transaction
                                }
                            }
                        fireFlyTransactionDataDao.insertAll(transactions)
                        for (transaction in data.transactionAttributes.transactions) {
                            tagSet.addAll(transaction.getTagList())
                            transaction.category_name?.let {
                                categorySet.add(Category(it))
                            }
                        }
                    }
                    tagsDao.insertAll(tagSet.toList())
                    categoryDao.insertAll(categorySet.toList())
                    if (
                        networkData.meta.pagination.total_pages !=
                        networkData.meta.pagination.current_page
                    ) {
                        pageKey += 1
                    } else {
                        loadData = false
                    }

                } else {
                    loadData = false
                }
            }
            pageKey -= 1
        }.await()
    }
}
