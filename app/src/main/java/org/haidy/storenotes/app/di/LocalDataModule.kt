package org.haidy.storenotes.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.haidy.storenotes.data.local.database.NotesDatabase
import org.haidy.storenotes.data.local.datastore.StoreNotesDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Singleton
    @Provides
    fun provideNotesDao(
        @ApplicationContext context: Context
    ) = NotesDatabase.getInstance(context).notesDao()

    @Singleton
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ) = StoreNotesDataStore(context)

}