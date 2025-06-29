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

import Constants.ACCOUNTS_API_ENDPOINT
import data.database.model.accounts.AccountsModel
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface AccountsService {

    @GET(ACCOUNTS_API_ENDPOINT)
    suspend fun getPaginatedAccountType(
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<AccountsModel>

    @GET(ACCOUNTS_API_ENDPOINT)
    suspend fun getNewPaginatedAccountType(
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<AccountsModel>

}