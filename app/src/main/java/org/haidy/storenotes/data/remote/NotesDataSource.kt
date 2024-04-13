package org.haidy.storenotes.data.remote

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import org.haidy.storenotes.data.remote.model.NoteNetwork
import org.haidy.storenotes.data.remote.model.NotesNetwork
import org.haidy.storenotes.data.util.toNetworkModel
import org.haidy.storenotes.data.util.toNetworkOrEmptyModel
import org.haidy.storenotes.repository.model.Note
import org.mobilenativefoundation.store.store5.FetcherResult
import javax.inject.Inject

class NotesDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userId: String
) {

    fun getAllUserNotes(): Flow<NotesNetwork> {
        val userNotesCollection = getUserNotesCollection()
            ?: return flow { emit(NotesNetwork("", emptyList())) }
        return userNotesCollection.snapshots().map { noteSnapshot ->
            noteSnapshot.toObject(NotesNetwork::class.java).toNetworkOrEmptyModel()
        }.catch { emit(NotesNetwork("", emptyList())) }
    }

    suspend fun getNote(id: String): FetcherResult<NoteNetwork> {
        val userNotesCollection = getUserNotesCollection()
        val note = userNotesCollection?.get()?.await()?.toObject(NotesNetwork::class.java)
            .toNetworkOrEmptyModel()
        val noteNetwork = note.notes.find { it.id == id }.toNetworkOrEmptyModel()
        return if (noteNetwork.id.isEmpty())
            FetcherResult.Error.Message("Note not found") else FetcherResult.Data(noteNetwork)
    }

    suspend fun addNewNote(note: Note) {
        val userNotesCollection = getUserNotesCollection()
        if(userNotesCollection == null){
            firestore.collection(NOTES_COLLECTION).document(userId).set(note.toNetworkModel()).await()
        }else{
            val notes = userNotesCollection.get().await().toObject(NotesNetwork::class.java)
                .toNetworkOrEmptyModel()
            val newNotes = notes.notes.toMutableList()
            newNotes.add(note.toNetworkModel())
            userNotesCollection.set(NotesNetwork(notes.authorId, newNotes)).await()

        }
    }

    suspend fun updateNote(note: Note){
        val userNotesCollection = getUserNotesCollection() ?: return
        val notes = userNotesCollection.get().await().toObject(NotesNetwork::class.java)
            .toNetworkOrEmptyModel()
        val noteIndex = notes.notes.map { it.id }.indexOf(note.id)
        val newNotes = notes.notes.toMutableList()
        newNotes[noteIndex] = note.toNetworkModel()
        userNotesCollection.set(NotesNetwork(notes.authorId, newNotes)).await()
    }

    suspend fun deleteNote(noteId: String){
        val userNotesCollection = getUserNotesCollection()
        val notes = userNotesCollection?.get()?.await()?.toObject(NotesNetwork::class.java)
            .toNetworkOrEmptyModel()
        val newNotes = notes.notes.filter { it.id != noteId }
        userNotesCollection?.set(NotesNetwork(userId, newNotes))?.await()
    }

    suspend fun deleteAllUserNotes() {
        getUserNotesCollection()?.delete()?.await()
    }

    private fun getUserNotesCollection(): DocumentReference?{
        return try{
            firestore.collection(NOTES_COLLECTION).document(userId)
        }catch (e: Exception){
            null
        }
    }


    companion object {
        const val NOTES_COLLECTION = "notes"
    }

}