package com.info85.infofacil.ui.lesson

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.info85.infofacil.data.ProgressRepository
import com.info85.infofacil.data.content.AppContent
import com.info85.infofacil.model.Lesson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LessonUiState(
    val lesson: Lesson? = null,
    val moduleTitle: String = "",
    val isCompleted: Boolean = false,
    val lessonIndex: Int = 0,
    val totalLessons: Int = 0,
    val nextLessonId: String? = null,
)

class LessonViewModel(
    application: Application,
    private val progressRepository: ProgressRepository,
    private val moduleId: String,
    private val lessonId: String,
) : AndroidViewModel(application) {

    private val module = AppContent.modules.firstOrNull { it.id == moduleId }
    private val lessonIndex = module?.lessons?.indexOfFirst { it.id == lessonId } ?: 0

    val uiState: StateFlow<LessonUiState> = progressRepository.allProgress()
        .combine(MutableStateFlow(Unit)) { progressMap, _ ->
            val completed = progressMap[moduleId]?.completedLessonIds?.contains(lessonId) ?: false
            val nextLesson = module?.lessons?.getOrNull(lessonIndex + 1)
            LessonUiState(
                lesson = module?.lessons?.getOrNull(lessonIndex),
                moduleTitle = module?.title ?: "",
                isCompleted = completed,
                lessonIndex = lessonIndex,
                totalLessons = module?.lessons?.size ?: 0,
                nextLessonId = nextLesson?.id,
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LessonUiState())

    fun markComplete() {
        viewModelScope.launch {
            progressRepository.markLessonComplete(moduleId, lessonId)
        }
    }
}

class LessonViewModelFactory(
    private val application: Application,
    private val progressRepository: ProgressRepository,
    private val moduleId: String,
    private val lessonId: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LessonViewModel(application, progressRepository, moduleId, lessonId) as T
    }
}
