package data.network

sealed interface NetworkResult<out T> {

    data class Data<out T>(val data: T) : NetworkResult<T>

    data class Loading<out T>(val data: T) : NetworkResult<T>

    sealed interface Error<out T> : NetworkResult<T> {

        class NetworkError<out T> : Error<T>
        class AuthExpired<out T> : Error<T>
    }
}