package com.info85.infofacil.ui.home

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

data class HomeUiState(
    val modules: List<Module> = emptyList(),
    val progress: Map<String, UserProgress> = emptyMap(),
    val totalLessonsCompleted: Int = 0,
    val totalModulesStarted: Int = 0,
    val totalModulesCompleted: Int = 0,
    val lastStartedModuleId: String? = null,
)

class HomeViewModel(
    application: Application,
    private val progressRepository: ProgressRepository,
) : AndroidViewModel(application) {

    val uiState: StateFlow<HomeUiState> = progressRepository.allProgress()
        .map { progressMap ->
            val modules = AppContent.modules
            val totalLessons = progressMap.values.sumOf { it.completedLessonIds.size }
            val startedModules = progressMap.values.count { it.completedLessonIds.isNotEmpty() || it.quizScore != null }
            val completedModules = progressMap.values.count { it.isModuleComplete }
            val lastStarted = progressMap.entries
                .filter { it.value.completedLessonIds.isNotEmpty() && !it.value.isModuleComplete }
                .firstOrNull()?.key

            HomeUiState(
                modules = modules,
                progress = progressMap,
                totalLessonsCompleted = totalLessons,
                totalModulesStarted = startedModules,
                totalModulesCompleted = completedModules,
                lastStartedModuleId = lastStarted,
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}

class HomeViewModelFactory(
    private val application: Application,
    private val progressRepository: ProgressRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(application, progressRepository) as T
    }
}
