package org.haidy.storenotes.data.remote

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import org.haidy.storenotes.data.local.datastore.StoreNotesDataStore
import org.haidy.storenotes.data.remote.model.NoteNetwork
import org.haidy.storenotes.data.remote.model.NotesNetwork
import org.haidy.storenotes.data.util.toNetworkModel
import org.haidy.storenotes.data.util.toNetworkOrEmptyModel
import org.haidy.storenotes.repository.model.Note
import org.mobilenativefoundation.store.store5.FetcherResult
import java.time.Instant
import javax.inject.Inject

class NotesDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storeNotesDataStore: StoreNotesDataStore
) {

    suspend fun getAllUserNotes(): Flow<NotesNetwork> {
        val userNotesDocument = getUserNotesDocument()
            ?: return flow { emit(NotesNetwork("", emptyList())) }
        return userNotesDocument.snapshots().map { noteSnapshot ->
            noteSnapshot.toObject(NotesNetwork::class.java).toNetworkOrEmptyModel()
        }.catch { emit(NotesNetwork("", emptyList())) }
    }

    suspend fun getNote(id: String): FetcherResult<NoteNetwork> {
        val userNotesDocument = getUserNotesDocument()
        val note = userNotesDocument?.get()?.await()?.toObject(NotesNetwork::class.java)
            .toNetworkOrEmptyModel()
        val noteNetwork = note.notes.find { it.id == id }.toNetworkOrEmptyModel()
        return if (noteNetwork.id.isEmpty())
            FetcherResult.Error.Message("Note not found") else FetcherResult.Data(noteNetwork)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addNewNote(note: Note) {
        val userId = getUserId()
        val noteId = Instant.now().toEpochMilli().toString()
        val userNotesDocument = getUserNotesDocument()
        if (userNotesDocument == null) {
            val notesNetwork = NotesNetwork(
                authorId = userId,
                notes = listOf(note.copy(id = noteId).toNetworkModel())
            )
            firestore.collection(NOTES_COLLECTION).document(userId)
                .set(notesNetwork).await()
        } else {
            val notes = userNotesDocument.get().await().toObject(NotesNetwork::class.java)
                .toNetworkOrEmptyModel()
            val newNotes = notes.notes.toMutableList()
            newNotes.add(note.copy(id = noteId).toNetworkModel())
            userNotesDocument.set(NotesNetwork(notes.authorId, newNotes)).await()
        }
    }

    suspend fun updateNote(note: Note) {
        val userNotesDocument = getUserNotesDocument() ?: return
        val notes = userNotesDocument.get().await().toObject(NotesNetwork::class.java)
            .toNetworkOrEmptyModel()
        val noteIndex = notes.notes.map { it.id }.indexOf(note.id)
        val newNotes = notes.notes.toMutableList()
        newNotes[noteIndex] = note.toNetworkModel()
        userNotesDocument.set(NotesNetwork(notes.authorId, newNotes)).await()
    }

    suspend fun deleteNote(noteId: String) {
        val userId = getUserId()
        val userNotesDocument = getUserNotesDocument()
        val notes = userNotesDocument?.get()?.await()?.toObject(NotesNetwork::class.java)
            .toNetworkOrEmptyModel()
        val newNotes = notes.notes.filter { it.id != noteId }
        userNotesDocument?.set(NotesNetwork(userId, newNotes))?.await()
    }

    suspend fun deleteAllUserNotes() {
        getUserNotesDocument()?.delete()?.await()
    }

    private suspend fun getUserNotesDocument(): DocumentReference? {
        val documentReference = firestore.collection(NOTES_COLLECTION).document(getUserId())
        val documentSnapshot = documentReference.get().await()
        return if (documentSnapshot.exists()) {
            documentReference
        } else {
            null
        }
    }


    private suspend fun getUserId(): String {
        return storeNotesDataStore.getUserId()
    }

    companion object {
        const val NOTES_COLLECTION = "notes"
    }

}