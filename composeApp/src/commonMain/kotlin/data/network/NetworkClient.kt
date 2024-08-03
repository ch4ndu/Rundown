package data.network

import de.jensklingenberg.ktorfit.Ktorfit
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

expect fun getKtorFit(
    baseUrl: String,
    accessToken: String
): Ktorfit

@ExperimentalSerializationApi
class NetworkClient {

    companion object {

        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            coerceInputValues = true
        }
    }
}