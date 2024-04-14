package org.haidy.storenotes.ui.screens.notes

sealed class NotesUiEffect {
    data class NavigateToNoteDetails(val noteId: String) : NotesUiEffect()
    data class ShowErrorMessage(val message: String) : NotesUiEffect()
}