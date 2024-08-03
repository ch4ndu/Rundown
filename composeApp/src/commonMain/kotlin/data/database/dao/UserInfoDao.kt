package data.database.dao

import androidx.room.Dao
import androidx.room.Query
import data.database.model.accounts.UserInfo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserInfoDao : BaseDao<UserInfo> {

    @Query("SELECT * FROM userInfo ORDER BY activeUser DESC")
    abstract suspend fun getAllUsers(): List<UserInfo>

    @Query("SELECT * FROM userInfo WHERE activeUser =:isActive")
    abstract suspend fun getCurrentActiveUserInfo(isActive: Boolean = true): UserInfo?

    @Query("SELECT * FROM userInfo WHERE activeUser =:isActive")
    abstract fun getCurrentActiveUserInfoFlow(isActive: Boolean = true): Flow<UserInfo?>

    @Query("SELECT userEmail FROM userInfo WHERE activeUser =:isActive")
    abstract suspend fun getCurrentActiveUserEmail(isActive: Boolean = true): String

    @Query("SELECT baseUrl FROM userInfo WHERE activeUser =:isActive")
    abstract suspend fun getCurrentActiveUserUrl(isActive: Boolean = true): String

//    @Query("UPDATE userInfo SET activeUser =:activeUser WHERE id =:userId")
//    abstract suspend fun updateActiveUser(userId: Long, activeUser: Boolean = true)

    @Query("DELETE FROM userInfo WHERE activeUser =:isActive")
    abstract suspend fun deleteCurrentUser(isActive: Boolean = true)
}