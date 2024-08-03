package domain


sealed class AuthState {
    data object Authenticated : AuthState()
    data class Error(val reason: String) : AuthState()
}