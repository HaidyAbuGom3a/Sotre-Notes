package org.haidy.storenotes.repository.model

sealed class NotesKey {
    sealed class Read : NotesKey() {
        data class ReadByNoteId(val noteId: String) : Read()
        data object ReadAllNotes : Read()
    }

    sealed class Write : NotesKey() {
        data object Create : Write()
        data object Update : Write()
    }

    sealed class Clear : NotesKey() {
        data object ClearAllNotes : Clear()
        data class ById(val noteId: String) : Clear()
    }
}