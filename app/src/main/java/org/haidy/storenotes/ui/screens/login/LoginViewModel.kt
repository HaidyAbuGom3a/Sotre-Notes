package org.haidy.storenotes.ui.screens.login

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import org.haidy.storenotes.repository.AuthRepository
import org.haidy.storenotes.repository.UserRepository
import org.haidy.storenotes.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel<LoginUiState, LoginUiEffect>(LoginUiState()), LoginInteractionListener {
    override fun onClickLogin() {
        val state = _state.value
        tryRequest(
            block = { authRepository.loginUser(state.email, state.password) },
            onSuccess = ::onLoginSuccess,
            onError = ::onError
        )
    }

    private fun onLoginSuccess(userId: String) {
        tryRequest(
            block = { userRepository.saveUserId(userId) },
            onSuccess = {
                _state.update { it.copy(isLoading = false) }
                sendNewEffect(LoginUiEffect.ShowSuccessMessage)
                sendNewEffect(LoginUiEffect.NavigateToNotesScreen)
            },
            onError = ::onError
        )
    }

    private fun onError(error: Exception) {
        _state.update { it.copy(isLoading = false) }
        sendNewEffect(LoginUiEffect.ShowErrorMessage(error.message ?: "Request failed"))
    }

    override fun onClickDoNotHaveAccount() {
        sendNewEffect(LoginUiEffect.NavigateToSignUp)
    }

    override fun onEmailChanged(email: String) {
        _state.update {
            it.copy(email = email)
        }
    }

    override fun onPasswordChanged(password: String) {
        _state.update {
            it.copy(password = password)
        }
    }

    override fun onClickEyeIcon() {
        val isPasswordVisible = _state.value.isPasswordVisible
        _state.update {
            it.copy(isPasswordVisible = !isPasswordVisible)
        }
    }
}