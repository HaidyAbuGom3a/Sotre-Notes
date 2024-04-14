package org.haidy.storenotes.ui.screens.note_details

sealed class NoteDetailsUiEffect {
    data object ShowSuccessMessage : NoteDetailsUiEffect()
    data class ShowErrorMessage(val message: String) : NoteDetailsUiEffect()
    data object NavigateBack : NoteDetailsUiEffect()
}