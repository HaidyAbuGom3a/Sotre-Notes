package org.haidy.storenotes.ui.screens.singup

sealed class SignUpUiEffect {
    data object NavigateToLogin: SignUpUiEffect()
    data object ShowSuccessMessage: SignUpUiEffect()
    data class ShowErrorMessage(val message: String): SignUpUiEffect()
}