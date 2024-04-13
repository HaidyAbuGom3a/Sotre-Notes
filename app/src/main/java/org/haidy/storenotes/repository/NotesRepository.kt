package org.haidy.storenotes.repository

import kotlinx.coroutines.flow.map
import org.haidy.storenotes.data.local.database.dao.NotesDao
import org.haidy.storenotes.data.remote.NotesDataSource
import org.haidy.storenotes.data.util.toEntity
import org.haidy.storenotes.data.util.toNote
import org.haidy.storenotes.repository.model.Note
import org.haidy.storenotes.repository.model.NotesKey
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val notesDataSource: NotesDataSource,
    private val notesDao: NotesDao,
    private val userId: String
) {
    fun getNoteStore(): Store<NotesKey, Any> {
        val fetcher = Fetcher.of { key: NotesKey ->
            require(key is NotesKey.Read)
            when (key) {
                is NotesKey.Read.ByNoteId -> notesDataSource.getNote(key.noteId)
                is NotesKey.Read.AllNotes -> notesDataSource.getAllUserNotes().map { notesNetwork ->
                    notesNetwork.notes.map { it.toEntity(userId) }
                }
            }
        }
        val sourceOfTruth = getStoreSourceOfTruth()
        return StoreBuilder.from(
            fetcher = fetcher,
            sourceOfTruth = sourceOfTruth
        ).build()
    }


    private fun getStoreSourceOfTruth() = SourceOfTruth.Companion.of(
        reader = { key: NotesKey ->
            require(key is NotesKey.Read)
            when (key) {
                is NotesKey.Read.AllNotes -> notesDao.getAllUserNotes(userId).map {
                    it.map { noteEntity -> noteEntity.toNote() }
                }

                is NotesKey.Read.ByNoteId -> notesDao.getNoteById(key.noteId).map { it?.toNote() }
            }
        },
        writer = { key: NotesKey, value: Any ->
            require(key is NotesKey.Write)
            when (key) {
                is NotesKey.Write.ById -> {
                    (value as Note)
                    notesDao.updateNote(value.toEntity(userId))
                }

                NotesKey.Write.Create -> {
                    (value as Note)
                    notesDao.addNote(value.toEntity(userId))
                }
            }
        },
        delete = { key: NotesKey ->
            require(key is NotesKey.Clear)
            when (key) {
                is NotesKey.Clear.AllNotes -> notesDao.deleteAllUserNotes(userId)
                is NotesKey.Clear.ById -> notesDao.deleteNoteById(key.noteId)
            }
        }
    )
}