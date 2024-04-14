package org.haidy.storenotes.app.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.haidy.storenotes.data.local.database.dao.NotesDao
import org.haidy.storenotes.data.local.datastore.StoreNotesDataStore
import org.haidy.storenotes.data.remote.NotesDataSource
import org.haidy.storenotes.repository.AuthRepository
import org.haidy.storenotes.repository.NotesRepository
import org.haidy.storenotes.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    suspend fun provideUserId(dataStore: StoreNotesDataStore): String {
        return dataStore.getUserId()
    }

    @Singleton
    @Provides
    fun provideNoteRepository(
        notesDataSource: NotesDataSource,
        notesDao: NotesDao,
        storeNotesDataStore: StoreNotesDataStore
    ): NotesRepository {
        return NotesRepository(notesDataSource, notesDao, storeNotesDataStore)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository {
        return AuthRepository(auth)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        dataStore: StoreNotesDataStore
    ): UserRepository {
        return UserRepository(dataStore)
    }

}