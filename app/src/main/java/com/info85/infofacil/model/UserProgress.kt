package com.info85.infofacil.model

data class UserProgress(
    val moduleId: String,
    val completedLessonIds: Set<String>,
    val quizScore: Int?,
    val quizTotalQuestions: Int,
    val isModuleComplete: Boolean,
)

val UserProgress.progressFraction: Float
    get() {
        if (completedLessonIds.isEmpty() && quizScore == null) return 0f
        val total = (completedLessonIds.size + 1).coerceAtLeast(1)
        return completedLessonIds.size.toFloat() / total
    }
