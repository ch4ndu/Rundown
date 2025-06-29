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

@file:OptIn(ExperimentalSerializationApi::class)

package data.network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.CallConverterFactory
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi

actual fun getKtorFit(
    baseUrl: String,
    accessToken: String
): Ktorfit {
    return ktorfit {
        httpClient(
            HttpClient(OkHttp) {
                defaultRequest {
                    url(baseUrl)
                    bearerAuth(accessToken)
                    header("Content-Type", "application/vnd.api+json")
                    header("Accept", "application/json")
                    header("User-Agent", "com.udnahc.fireflydesktop")
                }

                install(ContentNegotiation) { json(NetworkClient.json) }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
                Charsets { register(Charsets.UTF_8) }
            }
        )
        converterFactories(
            CallConverterFactory(),
            ResponseConverterFactory(),
            ResultConverterFactory()
        )
    }
}