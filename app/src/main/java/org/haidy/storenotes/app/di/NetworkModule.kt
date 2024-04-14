package org.haidy.storenotes.app.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.haidy.storenotes.data.local.datastore.StoreNotesDataStore
import org.haidy.storenotes.data.remote.NotesDataSource
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideFireBaseAuth() = Firebase.auth


    @Singleton
    @Provides
    fun provideFireBaseFireStore() = Firebase.firestore

    @Singleton
    @Provides
    fun provideNotesDataSource(fireStore: FirebaseFirestore, storeNotesDataStore: StoreNotesDataStore) =
        NotesDataSource(fireStore, storeNotesDataStore)
}