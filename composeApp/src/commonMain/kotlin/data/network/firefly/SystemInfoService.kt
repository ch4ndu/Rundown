package data.network.firefly

import Constants.SYSTEM_INFO_ENDPOINT
import data.database.model.userinfo.system.SystemInfoModel
import data.database.model.userinfo.user.UserDataModel
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET


interface SystemInfoService {

    @GET(SYSTEM_INFO_ENDPOINT)
    suspend fun getSystemInfo(): Response<SystemInfoModel>

    @GET("$SYSTEM_INFO_ENDPOINT/user")
    suspend fun getCurrentUserInfo(): Response<UserDataModel>

}