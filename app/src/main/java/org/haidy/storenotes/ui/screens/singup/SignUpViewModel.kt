package org.haidy.storenotes.ui.screens.singup

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import org.haidy.storenotes.repository.AuthRepository
import org.haidy.storenotes.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository) :
    BaseViewModel<SignUpUiState, SignUpUiEffect>(SignUpUiState()), SignUpInteractionListener {

    override fun onClickSignUp() {
        val state = _state.value
        tryRequest(
            block = { authRepository.signUpUser(state.email, state.password) },
            onSuccess = {
                _state.update {
                    it.copy(isLoading = false)
                }
                sendNewEffect(SignUpUiEffect.ShowSuccessMessage)
                sendNewEffect(SignUpUiEffect.NavigateToLogin)
            },
            onError = ::onSignUpError
        )
    }

    private fun onSignUpError(error: Exception) {
        _state.update { it.copy(isLoading = false) }
        sendNewEffect(SignUpUiEffect.ShowErrorMessage(error.message ?: "Request failed"))
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