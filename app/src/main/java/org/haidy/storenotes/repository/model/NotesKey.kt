package org.haidy.storenotes.repository.model

sealed class NotesKey {
    sealed class Read : NotesKey() {
        data class ByNoteId(val noteId: String) : Read()
        data class AllNotes(val authorId: String) : Read()
    }

    sealed class Write : NotesKey() {
        data object Create : Write()
        data class ById(val note: Note) : Write()
    }

    sealed class Clear : NotesKey() {
        data class AllNotes(val authorId: String) : Clear()
        data class ById(val noteId: String) : Clear()
    }
}