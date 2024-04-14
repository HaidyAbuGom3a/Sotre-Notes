package org.haidy.storenotes.ui.screens.note_details

import org.haidy.storenotes.repository.model.Note

data class NoteDetailsUiState(
    val noteId: String = "",
    val noteContent: String = "",
    val isLoading: Boolean = false
)

fun NoteDetailsUiState.toNote(): Note {
    return Note(
        id = noteId,
        content = noteContent
    )
}