package org.haidy.storenotes.ui.screens.notes

interface NotesInteractionListener {
    fun onClickNote(noteId: String)
    fun onClickAddNote()
    fun onClickDeleteAllNotes()
    fun onNewNoteContentChanged(content: String)
}