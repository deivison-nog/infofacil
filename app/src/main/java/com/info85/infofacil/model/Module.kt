package com.info85.infofacil.model

data class Module(
    val id: String,
    val title: String,
    val description: String,
    val lessons: List<Lesson>,
    val quiz: List<QuizQuestion>,
    val duration: String,
    val accent: ModuleAccent,
    val iconEmoji: String,
)

enum class ModuleAccent { Blue, Green, Orange, Purple, Teal, Rose }
