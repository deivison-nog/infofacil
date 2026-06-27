package com.info85.infofacil.navigation

sealed class Route(val path: String) {
    object Splash : Route("splash")
    object Onboarding : Route("onboarding")
    object Home : Route("home")
    object Modules : Route("modules")
    object Progress : Route("progress")

    data class ModuleDetail(val moduleId: String) : Route("module/{moduleId}") {
        companion object { const val PATH = "module/{moduleId}" }
        fun buildRoute() = "module/$moduleId"
    }

    data class LessonRoute(val moduleId: String, val lessonId: String) : Route("lesson/{moduleId}/{lessonId}") {
        companion object { const val PATH = "lesson/{moduleId}/{lessonId}" }
        fun buildRoute() = "lesson/$moduleId/$lessonId"
    }

    data class QuizRoute(val moduleId: String) : Route("quiz/{moduleId}") {
        companion object { const val PATH = "quiz/{moduleId}" }
        fun buildRoute() = "quiz/$moduleId"
    }
}
