package com.info85.infofacil.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "infofacil_preferences")

class ProgressRepository(private val context: Context) {

    fun progress(moduleId: String): Flow<Float> {
        val key = floatPreferencesKey("progress_$moduleId")
        return context.dataStore.data.map { preferences -> preferences[key] ?: 0f }
    }

    suspend fun saveProgress(moduleId: String, value: Float) {
        val key = floatPreferencesKey("progress_$moduleId")
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
