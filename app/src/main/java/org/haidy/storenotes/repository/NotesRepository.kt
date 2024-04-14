package org.haidy.storenotes.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.haidy.storenotes.data.local.database.dao.NotesDao
import org.haidy.storenotes.data.local.datastore.StoreNotesDataStore
import org.haidy.storenotes.data.remote.NotesDataSource
import org.haidy.storenotes.data.remote.model.NoteNetwork
import org.haidy.storenotes.data.util.toEntity
import org.haidy.storenotes.data.util.toNote
import org.haidy.storenotes.data.util.toNoteOrEmptyModel
import org.haidy.storenotes.repository.model.Note
import org.haidy.storenotes.repository.model.NotesKey
import org.mobilenativefoundation.store.core5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.FetcherResult
import org.mobilenativefoundation.store.store5.MutableStore
import org.mobilenativefoundation.store.store5.OnUpdaterCompletion
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.Updater
import org.mobilenativefoundation.store.store5.UpdaterResult
import org.mobilenativefoundation.store.store5.impl.extensions.asMutableStore
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val notesDataSource: NotesDataSource,
    private val notesDao: NotesDao,
    private val datastore: StoreNotesDataStore
) {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalStoreApi::class)
    suspend fun getNoteStore(): MutableStore<NotesKey, Any> {
        val fetchedNotes = fetchGetAllNotes()
        val fetcher: Fetcher<NotesKey, Any> = Fetcher.ofFlow { key: NotesKey ->
            require(key is NotesKey.Read)
            when (key) {
                is NotesKey.Read.ReadByNoteId -> flow { emit(fetchGetNoteById(key)) }
                is NotesKey.Read.ReadAllNotes -> fetchedNotes
            }
        }
        val sourceOfTruth = getStoreSourceOfTruth()
        return StoreBuilder.from(
            fetcher = fetcher,
            sourceOfTruth = sourceOfTruth
        ).build().asMutableStore<NotesKey, Any, Any, Any, Any>(
            updater = getStoreUpdater(),
            bookkeeper = null
        )
    }

    private suspend fun fetchGetNoteById(key: NotesKey): Note {
        require(key is NotesKey.Read.ReadByNoteId)
        val response = notesDataSource.getNote(key.noteId)
        return mapFetcherResultToNote(response)
    }

    private suspend fun fetchGetAllNotes(): Flow<List<Note>> {
        return notesDataSource.getAllUserNotes().map { notesNetwork ->
            notesNetwork.notes.map { it.toNote() }
        }
    }

    private fun mapFetcherResultToNote(fetcherResult: FetcherResult<NoteNetwork>) =
        when (fetcherResult) {
            is FetcherResult.Data -> fetcherResult.value.toNote()
            is FetcherResult.Error.Exception -> Note("", "")
            is FetcherResult.Error.Message -> Note("", "")
            else -> Note("", "")
        }


    private suspend fun getStoreSourceOfTruth(): SourceOfTruth<NotesKey, Any, Any> {
        val userId = getUserId()
        val daoNotes = notesDao.getAllUserNotes(userId)
        return SourceOfTruth.Companion.of(

            reader = { key: NotesKey ->
                Log.i("HAIDDYY", "i am in reader")
                require(key is NotesKey.Read)
                when (key) {
                    is NotesKey.Read.ReadAllNotes -> daoNotes.map { notes -> notes.map { it.toNote() } }
                    is NotesKey.Read.ReadByNoteId -> notesDao.getNoteById(key.noteId).map { it.toNoteOrEmptyModel() }
                }
            },
            writer = { key: NotesKey, value: Any ->
                Log.i("HAIDDYY", "i am in writer, key is $key")
                when (key) {
                    is NotesKey.Write.Update -> {
                        value as Note
                        notesDao.updateNote(value.toEntity(getUserId()))
                    }

                    NotesKey.Write.Create -> {
                        value as Note
                        notesDao.addNote(value.toEntity(getUserId()))
                    }

                    NotesKey.Read.ReadAllNotes -> {
                        Log.i("HAIDDYY", "value is $value")
                        value as List<Note>
                        notesDao.updateNotes(
                            value.map { note -> note.toEntity(getUserId()) },
                            userId
                        )
                    }

                    is NotesKey.Read.ReadByNoteId -> {
                        value as Note
                        notesDao.updateNote(value.toEntity(getUserId()))
                    }

                    else -> {}
                }
            },
            delete = { key: NotesKey ->
                require(key is NotesKey.Clear)
                when (key) {
                    is NotesKey.Clear.ClearAllNotes -> notesDao.deleteAllUserNotes(userId)
                    is NotesKey.Clear.ById -> notesDao.deleteNoteById(key.noteId)
                }
            }
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStoreUpdater() =
        Updater.by(
            post = { key: NotesKey, note: Any ->
                require(key is NotesKey.Write || key is NotesKey.Clear)
                note as Note
                when (key) {
                    is NotesKey.Write.Create -> UpdaterResult.Success.Untyped(
                        notesDataSource.addNewNote(
                            note
                        )
                    )

                    is NotesKey.Write.Update -> UpdaterResult.Success.Untyped(
                        notesDataSource.updateNote(
                            note
                        )
                    )

                    is NotesKey.Clear.ClearAllNotes -> UpdaterResult.Success.Untyped(
                        notesDataSource.deleteAllUserNotes()
                    )

                    is NotesKey.Clear.ById -> UpdaterResult.Success.Untyped(
                        notesDataSource.deleteNote(
                            key.noteId
                        )
                    )

                    else -> UpdaterResult.Error.Message("Updater Failed")
                }
            },
            onCompletion = OnUpdaterCompletion(
                onSuccess = { Log.i("HAIDDYY", "Updating success") },
                onFailure = { Log.i("HAIDDYY", "Updating failed") }
            )
        )

    private suspend fun getUserId() = datastore.getUserId()
}
