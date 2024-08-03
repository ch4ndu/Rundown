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
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
actual fun getKtorFit(
    baseUrl: String,
    accessToken: String
): Ktorfit {
    val client = OkHttpClient().newBuilder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    return ktorfit {
        httpClient(
            HttpClient(OkHttp) {
                engine { preconfigured = client.build() }
                defaultRequest {
                    url(baseUrl)
                    bearerAuth(accessToken)
                    header("Content-Type", "application/vnd.api+json")
                    header("Accept", "application/json")
                    header("User-Agent", "com.udnahc.fireflydroid")
                }

                install(ContentNegotiation) { json(NetworkClient.json) }
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