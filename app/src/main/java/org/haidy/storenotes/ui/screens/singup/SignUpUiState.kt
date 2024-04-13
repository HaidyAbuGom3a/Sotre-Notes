package org.haidy.storenotes.ui.screens.singup

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = ""
)
