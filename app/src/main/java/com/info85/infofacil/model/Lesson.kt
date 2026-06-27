package com.info85.infofacil.model

data class Lesson(
    val id: String,
    val moduleId: String,
    val title: String,
    val objective: String,
    val content: String,
    val visualTitle: String,
    val visualContent: String,
    val tip: String,
    val order: Int,
)
