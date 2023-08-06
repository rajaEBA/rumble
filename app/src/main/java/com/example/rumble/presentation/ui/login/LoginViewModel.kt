package com.example.rumble.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumble.domain.common.Result
import com.example.rumble.domain.model.ProfileInfo
import com.example.rumble.domain.usecases.AuthorizeTokenUseCase
import com.example.rumble.domain.usecases.LoginViaCredentialUseCase
import com.example.rumble.infrastructure.api.LoginCredentials
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginViaCredentialUseCase: LoginViaCredentialUseCase,
    private val authorizeTokenUseCase: AuthorizeTokenUseCase
) : ViewModel() {

    private val mutableViewState = MutableSharedFlow<LoginState>()
    val loginViewState: SharedFlow<LoginState> = mutableViewState

    private fun login(credential: LoginCredentials) {
        viewModelScope.launch {
            if (credential.email.isEmpty() || credential.password.isEmpty()) {
                mutableViewState.emit(LoginState.NoResults)
            }

            when (val result = loginViaCredentialUseCase.invoke(credential)) {
                is Result.Success -> {
                    verifyToken(result.data.accessToken)
                }
                is Result.Error -> {
                    mutableViewState.emit(LoginState.FailedLogin("Failed login"))
                }
            }
        }
    }

    private fun verifyToken(token: String) {
        viewModelScope.launch {
            when (val result = authorizeTokenUseCase.invoke(token)) {
                is Result.Success -> {
                    mutableViewState.emit(LoginState.LunchMain(result.data))
                }
                is Result.Error -> {
                    mutableViewState.emit(LoginState.FailedLogin("Failed login"))
                }
            }
        }
    }

    fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.Login -> login(event.credential)
            }
        }
    }

    sealed class Event {
        data class Login(val credential: LoginCredentials) : Event()
    }

    sealed class LoginState {
        object NoResults : LoginState()
        data class FailedLogin(val error: String) : LoginState()
        data class LunchMain(val profile: ProfileInfo) : LoginState()
    }
}