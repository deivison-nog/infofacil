package com.info85.infofacil.model

data class Module(
    val id: String,
    val title: String,
    val description: String,
    val lessons: Int,
    val duration: String,
    val progress: Float,
    val accent: ModuleAccent,
    val completed: Boolean = false,
)

enum class ModuleAccent {
    Blue,
    Green,
    Orange,
    Purple,
}
