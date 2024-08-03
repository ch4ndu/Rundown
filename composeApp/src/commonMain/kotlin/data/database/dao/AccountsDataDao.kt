package data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import data.database.model.accounts.AccountData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AccountsDataDao : BaseDao<AccountData> {

    @Query("SELECT * FROM accounts WHERE name =:accountName AND type =:accountType")
    abstract suspend fun getAccountByNameAndType(
        accountName: String,
        accountType: String
    ): AccountData

    @Query("SELECT * FROM accounts WHERE type =:accountType ORDER BY id ASC")
    abstract fun getAccountsByType(accountType: String): Flow<List<AccountData>>

//    @Query("SELECT * FROM accounts WHERE type =:accountType ORDER BY id ASC")
//    abstract suspend fun getAccountsByTypee(accountType: String): PagingSource<Int, AccountData>

    @Query("SELECT * FROM accounts WHERE type =:accountType ORDER BY id ASC")
    abstract fun getAccountsFlowByType(accountType: String): Flow<List<AccountData>>

    @Query("DELETE FROM accounts WHERE type =:accountType AND isPending IS NOT :isPending")
    abstract suspend fun deleteAccountByType(
        accountType: String,
        isPending: Boolean = true
    ): Int

    @Query("SELECT * FROM accounts WHERE name =:name")
    abstract fun getAccountFlowForName(name: String): Flow<AccountData?>
}