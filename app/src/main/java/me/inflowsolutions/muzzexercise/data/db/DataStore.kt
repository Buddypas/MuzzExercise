package me.inflowsolutions.muzzexercise.data.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CurrentUserDataStore @Inject constructor(@ApplicationContext private val context: Context) {
    private val currentUserIdPreferencesKey = intPreferencesKey(CURRENT_USER_ID_KEY)

    private val currentUserIdFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[currentUserIdPreferencesKey] ?: 1
        }

    fun getCurrentUserId(): Flow<Int> = currentUserIdFlow

    suspend fun setCurrentUserId(userId: Int) {
        context.dataStore.edit { settings ->
            settings[currentUserIdPreferencesKey] = userId
        }
    }

    private companion object {
        const val CURRENT_USER_ID_KEY = "current_user_id"
    }
}