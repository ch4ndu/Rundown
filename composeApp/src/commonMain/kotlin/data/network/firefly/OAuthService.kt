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