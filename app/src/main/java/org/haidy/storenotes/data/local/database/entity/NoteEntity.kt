package org.haidy.storenotes.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NOTES_TABLE")
data class NoteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val content: String,
    val authorId: String
)
