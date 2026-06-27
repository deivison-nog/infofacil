package com.info85.infofacil.ui.modules

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

data class ModulesUiState(
    val modules: List<Module> = emptyList(),
    val progress: Map<String, UserProgress> = emptyMap(),
)

class ModulesViewModel(
    application: Application,
    private val progressRepository: ProgressRepository,
) : AndroidViewModel(application) {

    val uiState: StateFlow<ModulesUiState> = progressRepository.allProgress()
        .map { progressMap ->
            ModulesUiState(
                modules = AppContent.modules,
                progress = progressMap,
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ModulesUiState())
}

class ModulesViewModelFactory(
    private val application: Application,
    private val progressRepository: ProgressRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ModulesViewModel(application, progressRepository) as T
    }
}
