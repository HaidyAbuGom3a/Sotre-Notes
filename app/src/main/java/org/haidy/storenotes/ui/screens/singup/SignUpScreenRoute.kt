package org.haidy.storenotes.ui.screens.singup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.haidy.storenotes.app.navigation.Destination

fun NavController.navigateToSignUp() {
    navigate(Destination.SIGN_UP)
}

fun NavGraphBuilder.signupScreenRoute() {
    composable(Destination.SIGN_UP) { SignUpScreen() }
}