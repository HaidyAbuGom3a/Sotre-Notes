package org.haidy.storenotes.ui.screens.singup

interface SignUpInteractionListener {
    fun onClickSignUp()
    fun onEmailChanged(email: String)
    fun onPasswordChanged(password: String)
    fun onClickEyeIcon()
}