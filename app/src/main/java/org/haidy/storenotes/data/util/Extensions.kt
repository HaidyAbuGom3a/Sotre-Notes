package org.haidy.storenotes.data.util

import org.haidy.storenotes.data.local.database.entity.NoteEntity
import org.haidy.storenotes.data.remote.model.NoteNetwork
import org.haidy.storenotes.data.remote.model.NotesNetwork
import org.haidy.storenotes.repository.model.Note

fun NoteNetwork?.toNetworkOrEmptyModel(): NoteNetwork {
    return this ?: NoteNetwork("", "")
}

fun NotesNetwork?.toNetworkOrEmptyModel(): NotesNetwork {
    return this ?: NotesNetwork(authorId = "", notes = emptyList())
}

fun NoteNetwork.toEntity(authorId: String): NoteEntity {
    return NoteEntity(id, authorId, content)
}

fun NoteEntity.toNote(): Note {
    return Note(id, content)
}

fun Note.toNetworkModel(): NoteNetwork {
    return NoteNetwork(id, content)
}

fun Note.toEntity(authorId: String): NoteEntity {
    return NoteEntity(
        id = id,
        content = content,
        authorId = authorId

    )
}