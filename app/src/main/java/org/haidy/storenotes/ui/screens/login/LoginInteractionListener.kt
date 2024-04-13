package org.haidy.storenotes.ui.screens.login

interface LoginInteractionListener {
    fun onClickLogin()
    fun onClickDoNotHaveAccount()
    fun onEmailChanged(email: String)
    fun onPasswordChanged(password: String)
    fun onClickEyeIcon()
}