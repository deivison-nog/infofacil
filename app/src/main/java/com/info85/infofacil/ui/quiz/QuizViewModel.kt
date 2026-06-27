package com.info85.infofacil.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.info85.infofacil.data.ProgressRepository
import com.info85.infofacil.data.content.AppContent
import com.info85.infofacil.model.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: Int? = null,
    val isAnswered: Boolean = false,
    val score: Int = 0,
    val isFinished: Boolean = false,
    val moduleTitle: String = "",
)

class QuizViewModel(
    application: Application,
    private val progressRepository: ProgressRepository,
    private val moduleId: String,
) : AndroidViewModel(application) {

    private val module = AppContent.modules.firstOrNull { it.id == moduleId }

    private val _uiState = MutableStateFlow(
        QuizUiState(
            questions = module?.quiz ?: emptyList(),
            moduleTitle = module?.title ?: "",
        )
    )
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun answerQuestion(optionIndex: Int) {
        val state = _uiState.value
        if (state.isAnswered) return
        val isCorrect = state.questions.getOrNull(state.currentIndex)?.correctIndex == optionIndex
        _uiState.update {
            it.copy(
                selectedAnswer = optionIndex,
                isAnswered = true,
                score = if (isCorrect) it.score + 1 else it.score,
            )
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1
        if (nextIndex >= state.questions.size) {
            finishQuiz()
        } else {
            _uiState.update {
                it.copy(
                    currentIndex = nextIndex,
                    selectedAnswer = null,
                    isAnswered = false,
                )
            }
        }
    }

    fun finishQuiz() {
        val state = _uiState.value
        _uiState.update { it.copy(isFinished = true) }
        viewModelScope.launch {
            progressRepository.saveQuizResult(moduleId, state.score, state.questions.size)
        }
    }

    fun restartQuiz() {
        _uiState.update {
            QuizUiState(
                questions = module?.quiz ?: emptyList(),
                moduleTitle = module?.title ?: "",
            )
        }
    }
}

class QuizViewModelFactory(
    private val application: Application,
    private val progressRepository: ProgressRepository,
    private val moduleId: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return QuizViewModel(application, progressRepository, moduleId) as T
    }
}
