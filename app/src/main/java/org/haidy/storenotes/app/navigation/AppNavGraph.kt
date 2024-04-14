package org.haidy.storenotes.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import org.haidy.storenotes.app.LocalNavController
import org.haidy.storenotes.ui.screens.login.loginScreenRoute
import org.haidy.storenotes.ui.screens.note_details.noteDetailsScreenRoute
import org.haidy.storenotes.ui.screens.notes.notesScreenRoute
import org.haidy.storenotes.ui.screens.singup.signupScreenRoute

@Composable
fun AppNavGraph(startDestination: String) {
    NavHost(navController = LocalNavController.current, startDestination = startDestination) {
        loginScreenRoute()
        signupScreenRoute()
        notesScreenRoute()
        noteDetailsScreenRoute()
    }
}