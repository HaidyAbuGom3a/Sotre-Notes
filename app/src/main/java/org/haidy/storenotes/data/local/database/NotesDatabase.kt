package org.haidy.storenotes.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.haidy.storenotes.data.local.database.dao.NotesDao
import org.haidy.storenotes.data.local.database.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NotesDatabase: RoomDatabase(){
    abstract fun notesDao(): NotesDao

    companion object{
        private const val DATABASE_NAME = "notes_database"
        @Volatile private var instance: NotesDatabase? = null
        fun getInstance(context: Context): NotesDatabase{
            return instance ?: synchronized(this){
                buildDatabase(context).also{ instance = it }
            }

        }

        private fun buildDatabase(context: Context): NotesDatabase{
            return Room.databaseBuilder(context, NotesDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}