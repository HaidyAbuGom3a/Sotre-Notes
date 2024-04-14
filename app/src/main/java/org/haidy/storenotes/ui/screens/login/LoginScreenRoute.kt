package org.haidy.storenotes.ui.screens.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.haidy.storenotes.app.navigation.Destination

fun NavController.navigateToLogin(clearBackStack: Boolean = false) {
    navigate(Destination.LOGIN) {
        if (clearBackStack) {
            popUpTo(graph.id) {
                inclusive = true
            }
        }
    }
}

fun NavGraphBuilder.loginScreenRoute() {
    composable(Destination.LOGIN) { LoginScreen() }
}