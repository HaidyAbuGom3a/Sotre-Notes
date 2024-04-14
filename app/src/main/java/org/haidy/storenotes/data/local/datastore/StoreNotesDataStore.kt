package org.haidy.storenotes.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreNotesDataStore @Inject constructor(context: Context) {
    companion object {
        private const val PREFERENCES_FILE_NAME = "store_notes"
        private val USER_ID = stringPreferencesKey("user_id")
    }

    private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
        PREFERENCES_FILE_NAME
    )
    private val prefDataStore = context.preferencesDataStore
    suspend fun saveUserId(userId: String) {
        prefDataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun getUserId(): String {
        return prefDataStore.data.map { preferences -> preferences[USER_ID] }.first() ?: ""
    }


}