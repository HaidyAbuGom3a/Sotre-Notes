package org.haidy.storenotes.ui.screens.note_details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.haidy.storenotes.app.navigation.Destination

fun NavController.navigateToNoteDetails(noteId: String) {
    navigate("${Destination.NOTE_DETAILS}/$noteId")
}

fun NavGraphBuilder.noteDetailsScreenRoute() {
    composable(
        "${Destination.NOTE_DETAILS}/{${NoteDetailsArgs.NOTE_ID}}",
        arguments = listOf(navArgument(NoteDetailsArgs.NOTE_ID) { NavType.StringType })
    ) { NoteDetailsScreen() }
}

class NoteDetailsArgs(savedStateHandle: SavedStateHandle) {
    val noteId: String = savedStateHandle[NOTE_ID]!!

    companion object {
        const val NOTE_ID = "noteId"
    }
}