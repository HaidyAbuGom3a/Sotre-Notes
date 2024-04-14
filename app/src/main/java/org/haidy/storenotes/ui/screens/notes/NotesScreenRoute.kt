package org.haidy.storenotes.ui.screens.notes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.haidy.storenotes.app.navigation.Destination

fun NavController.navigateToNotes(clearBackStack: Boolean = false) {
    navigate(Destination.NOTES){
        if (clearBackStack) {
            popUpTo(graph.id) {
                inclusive = true
            }
        }
    }
}

fun NavGraphBuilder.notesScreenRoute() {
    composable(Destination.NOTES) { NotesScreen() }
}