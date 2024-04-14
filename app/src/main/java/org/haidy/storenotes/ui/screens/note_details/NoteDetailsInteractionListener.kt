package org.haidy.storenotes.ui.screens.note_details

interface NoteDetailsInteractionListener {
    fun onContentChanged(content: String)
    fun onClickDeleteNote()
    fun onClickUpdateNote()
}