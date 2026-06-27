package com.info85.infofacil.model

data class QuizQuestion(
    val id: String,
    val moduleId: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
)
