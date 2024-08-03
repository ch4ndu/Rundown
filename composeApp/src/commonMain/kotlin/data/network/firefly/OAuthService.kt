package data.network.firefly

import Constants.OAUTH_API_ENDPOINT
import data.database.model.auth.AuthModel
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.POST

interface OAuthService {

    @FormUrlEncoded
    @POST("$OAUTH_API_ENDPOINT/token")
    suspend fun getAccessToken(
        @Field("code") code: String,
        @Field("client_id") clientId: String?,
        @Field("client_secret") clientSecret: String?,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String? = "authorization_code"
    ): Response<AuthModel>

    @FormUrlEncoded
    @POST("$OAUTH_API_ENDPOINT/token")
    suspend fun getRefreshToken(
        @Field("grant_type") grantType: String? = "refresh_token",
        @Field("refresh_token") refreshToken: String?,
        @Field("client_id") clientId: String?,
        @Field("client_secret") clientSecret: String?
    ): Response<AuthModel>

}