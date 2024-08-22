package app.viewmodel

import androidx.lifecycle.viewModelScope
import di.DispatcherProvider
import domain.AuthState
import domain.repository.AuthRepository
import domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class AuthViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel() {

    val uiState: MutableStateFlow<AuthScreenState> = MutableStateFlow(AuthScreenState.Idle)

    open fun checkAuth() {
        uiState.value = AuthScreenState.InProgress
        viewModelScope.launch(dispatcherProvider.default) {
            if (userRepository.getCurrentActiveUserInfo(true)?.userEmail?.isNotEmpty() == true) {
                uiState.value = AuthScreenState.Authenticated
            } else {
                uiState.value = AuthScreenState.Idle
            }
        }
    }

    open fun tryAuth(
        url: String,
        token: String
    ) {
        viewModelScope.launch(dispatcherProvider.default) {
            if (url.isEmpty()) {
                uiState.value = AuthScreenState.Error("Url cannot be empty")
                return@launch
            }
            if (url.endsWith("/")) {
                uiState.value =
                    AuthScreenState.Error("Url cannot have a trailing slash. Valid format is http://192.168.0.1")
                return@launch
            }
            if (token.isEmpty()) {
                uiState.value = AuthScreenState.Error("token cannot be empty")
                return@launch
            }

            when (val authState = authRepository.tryAuth(url, token)) {
                is AuthState.Authenticated -> {
//                    unloadKoinModules(networkModule)
//                    loadKoinModules(networkModule)
                    uiState.value = AuthScreenState.Authenticated
                }

                is AuthState.Error -> uiState.value = AuthScreenState.Error(authState.reason)
            }
        }
    }
}

sealed class AuthScreenState {
    data object Idle : AuthScreenState()
    data object Authenticated : AuthScreenState()
    data object InProgress : AuthScreenState()
    data class Error(val reason: String) : AuthScreenState()
}