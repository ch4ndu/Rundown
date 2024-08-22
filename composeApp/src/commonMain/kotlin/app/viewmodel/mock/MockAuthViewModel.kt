package app.viewmodel.mock

import androidx.lifecycle.viewModelScope
import app.viewmodel.AuthScreenState
import app.viewmodel.AuthViewModel
import di.DispatcherProvider
import domain.repository.AuthRepository
import domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MockAuthViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val dispatcherProvider: DispatcherProvider,
) : AuthViewModel(userRepository, authRepository, dispatcherProvider) {

    override fun checkAuth() {
        uiState.value = AuthScreenState.InProgress
        viewModelScope.launch(dispatcherProvider.default) {
            delay(2_000)
            uiState.value = AuthScreenState.Authenticated
        }
    }

    override fun tryAuth(
        url: String,
        token: String
    ) {
        viewModelScope.launch(dispatcherProvider.default) {
            delay(5_000)
            uiState.value = AuthScreenState.Authenticated
        }
    }
}