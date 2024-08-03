package domain.repository

import data.database.dao.UserInfoDao
import data.database.model.accounts.UserInfo
import kotlinx.coroutines.flow.Flow


class UserRepository(private val userInfoDao: UserInfoDao) {

    fun getCurrentActiveUserInfoFlow(isActive: Boolean = true): Flow<UserInfo?> {
        return userInfoDao.getCurrentActiveUserInfoFlow(isActive)
    }

    suspend fun getCurrentActiveUserInfo(isActive: Boolean = true): UserInfo? {
        return userInfoDao.getCurrentActiveUserInfo(isActive)
    }

    suspend fun deleteCurrentUser(isActive: Boolean = true) {
        userInfoDao.deleteCurrentUser(isActive)
    }

    suspend fun insert(userInfo: UserInfo) {
        userInfoDao.insert(userInfo)
    }
}