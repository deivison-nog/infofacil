package com.info85.infofacil.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.info85.infofacil.data.content.AppContent
import com.info85.infofacil.model.UserProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "infofacil_preferences")

class ProgressRepository(private val context: Context) {

    fun allProgress(): Flow<Map<String, UserProgress>> {
        return context.dataStore.data.map { prefs ->
            AppContent.modules.associate { module ->
                val completedKey = stringPreferencesKey("completed_lessons_${module.id}")
                val scoreKey = intPreferencesKey("quiz_score_${module.id}")
                val totalKey = intPreferencesKey("quiz_total_${module.id}")

                val completedStr = prefs[completedKey] ?: ""
                val completedIds = if (completedStr.isBlank()) emptySet()
                                   else completedStr.split(",").toSet()
                val quizScore = if (prefs[scoreKey] != null) prefs[scoreKey] else null
                val quizTotal = prefs[totalKey] ?: 0
                val isComplete = completedIds.size == module.lessons.size && quizScore != null

                module.id to UserProgress(
                    moduleId = module.id,
                    completedLessonIds = completedIds,
                    quizScore = quizScore,
                    quizTotalQuestions = quizTotal,
                    isModuleComplete = isComplete,
                )
            }
        }
    }

    suspend fun markLessonComplete(moduleId: String, lessonId: String) {
        val key = stringPreferencesKey("completed_lessons_$moduleId")
        context.dataStore.edit { prefs ->
            val current = prefs[key] ?: ""
            val ids = if (current.isBlank()) mutableSetOf() else current.split(",").toMutableSet()
            ids.add(lessonId)
            prefs[key] = ids.joinToString(",")
        }
    }

    suspend fun saveQuizResult(moduleId: String, score: Int, total: Int) {
        val scoreKey = intPreferencesKey("quiz_score_$moduleId")
        val totalKey = intPreferencesKey("quiz_total_$moduleId")
        context.dataStore.edit { prefs ->
            prefs[scoreKey] = score
            prefs[totalKey] = total
        }
    }

    fun isOnboardingComplete(): Flow<Boolean> {
        val key = booleanPreferencesKey("onboarding_complete")
        return context.dataStore.data.map { prefs -> prefs[key] ?: false }
    }

    suspend fun setOnboardingComplete() {
        val key = booleanPreferencesKey("onboarding_complete")
        context.dataStore.edit { prefs -> prefs[key] = true }
    }
}
