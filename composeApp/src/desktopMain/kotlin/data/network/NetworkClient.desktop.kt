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