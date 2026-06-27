package com.info85.infofacil.ui.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.info85.infofacil.data.ProgressRepository
import com.info85.infofacil.data.content.AppContent
import com.info85.infofacil.model.Module
import com.info85.infofacil.model.UserProgress
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val isUnlocked: Boolean,
)

data class ProgressUiState(
    val modules: List<Module> = emptyList(),
    val progress: Map<String, UserProgress> = emptyMap(),
    val totalLessonsCompleted: Int = 0,
    val totalLessons: Int = 0,
    val totalModulesCompleted: Int = 0,
    val totalModules: Int = 0,
    val quizzesTaken: Int = 0,
    val achievements: List<Achievement> = emptyList(),
    val overallFraction: Float = 0f,
)

class ProgressViewModel(
    application: Application,
    private val progressRepository: ProgressRepository,
) : AndroidViewModel(application) {

    val uiState: StateFlow<ProgressUiState> = progressRepository.allProgress()
        .map { progressMap ->
            val modules = AppContent.modules
            val totalLessons = modules.sumOf { it.lessons.size }
            val completedLessons = progressMap.values.sumOf { it.completedLessonIds.size }
            val completedModules = progressMap.values.count { it.isModuleComplete }
            val quizzesTaken = progressMap.values.count { it.quizScore != null }
            val overallFraction = if (totalLessons > 0) completedLessons.toFloat() / totalLessons else 0f

            val achievements = buildAchievements(progressMap, completedLessons, completedModules, quizzesTaken)

            ProgressUiState(
                modules = modules,
                progress = progressMap,
                totalLessonsCompleted = completedLessons,
                totalLessons = totalLessons,
                totalModulesCompleted = completedModules,
                totalModules = modules.size,
                quizzesTaken = quizzesTaken,
                achievements = achievements,
                overallFraction = overallFraction,
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProgressUiState())

    private fun buildAchievements(
        progressMap: Map<String, UserProgress>,
        completedLessons: Int,
        completedModules: Int,
        quizzesTaken: Int,
    ): List<Achievement> = listOf(
        Achievement(
            id = "first_step",
            title = "Primeiro Passo",
            description = "Completou sua primeira aula",
            emoji = "🌟",
            isUnlocked = completedLessons >= 1,
        ),
        Achievement(
            id = "five_lessons",
            title = "Estudante",
            description = "Completou 5 aulas",
            emoji = "📚",
            isUnlocked = completedLessons >= 5,
        ),
        Achievement(
            id = "ten_lessons",
            title = "Dedicado",
            description = "Completou 10 aulas",
            emoji = "🎯",
            isUnlocked = completedLessons >= 10,
        ),
        Achievement(
            id = "first_quiz",
            title = "Primeiro Quiz",
            description = "Realizou seu primeiro quiz",
            emoji = "🧩",
            isUnlocked = quizzesTaken >= 1,
        ),
        Achievement(
            id = "first_module",
            title = "Módulo Concluído",
            description = "Concluiu um módulo completo",
            emoji = "🏆",
            isUnlocked = completedModules >= 1,
        ),
        Achievement(
            id = "three_modules",
            title = "Meio Caminho",
            description = "Concluiu 3 módulos",
            emoji = "🚀",
            isUnlocked = completedModules >= 3,
        ),
        Achievement(
            id = "all_modules",
            title = "Mestre Digital",
            description = "Concluiu todos os módulos!",
            emoji = "🎓",
            isUnlocked = completedModules >= 6,
        ),
    )
}

class ProgressViewModelFactory(
    private val application: Application,
    private val progressRepository: ProgressRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProgressViewModel(application, progressRepository) as T
    }
}
