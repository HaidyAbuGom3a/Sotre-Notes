package org.haidy.storenotes.repository.model

data class Notes(
    val authorId: String,
    val notes: List<Note>
)
