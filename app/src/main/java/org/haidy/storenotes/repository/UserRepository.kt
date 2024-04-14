package org.haidy.storenotes.repository

import org.haidy.storenotes.data.local.datastore.StoreNotesDataStore
import javax.inject.Inject

class UserRepository @Inject constructor(private val dataStore: StoreNotesDataStore) {

    suspend fun saveUserId(userId: String) {
        dataStore.saveUserId(userId)
    }

    suspend fun getUserId(): String {
        return dataStore.getUserId()
    }

}