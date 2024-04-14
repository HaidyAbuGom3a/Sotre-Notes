package org.haidy.storenotes.ui.screens.notes

import org.haidy.storenotes.repository.model.Note
import org.haidy.storenotes.repository.model.NotesKey
import org.mobilenativefoundation.store.store5.MutableStore

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val newNoteContent: String = "",
    val isLoading: Boolean = false,
)
