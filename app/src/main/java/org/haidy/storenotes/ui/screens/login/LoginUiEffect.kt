package org.haidy.storenotes.ui.screens.login

sealed class LoginUiEffect {
    data object NavigateToSignUp : LoginUiEffect()
    data object NavigateToNotesScreen: LoginUiEffect()
    data object ShowSuccessMessage: LoginUiEffect()
    data class ShowErrorMessage(val message: String): LoginUiEffect()
}