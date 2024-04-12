package org.haidy.storenotes.data.remote.model

data class NotesNetwork(
    val authorId: String,
    val notes: List<NoteNetwork>
)
