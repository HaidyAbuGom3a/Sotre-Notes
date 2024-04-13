package org.haidy.storenotes.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.haidy.storenotes.data.local.database.entity.NoteEntity

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: NoteEntity)

    @Query("DELETE FROM NOTES_TABLE WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: String)

    @Query("DELETE FROM NOTES_TABLE WHERE authorId = :authorId")
    fun deleteAllUserNotes(authorId: String)

    @Query("SELECT * FROM NOTES_TABLE WHERE authorId = :authorId")
    fun getAllUserNotes(authorId: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM NOTES_TABLE WHERE id = :noteId")
    fun getNoteById(noteId: String): Flow<NoteEntity?>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNote(note: NoteEntity)




}