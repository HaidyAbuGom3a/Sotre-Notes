package org.haidy.storenotes.ui.screens.singup

sealed class SignUpUiEffect {
    data object NavigateToLogin: SignUpUiEffect()
}