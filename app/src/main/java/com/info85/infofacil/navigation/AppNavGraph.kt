package com.info85.infofacil.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.info85.infofacil.data.ProgressRepository
import com.info85.infofacil.ui.home.HomeScreen
import com.info85.infofacil.ui.home.HomeViewModel
import com.info85.infofacil.ui.home.HomeViewModelFactory
import com.info85.infofacil.ui.lesson.LessonScreen
import com.info85.infofacil.ui.lesson.LessonViewModel
import com.info85.infofacil.ui.lesson.LessonViewModelFactory
import com.info85.infofacil.ui.modules.ModuleDetailScreen
import com.info85.infofacil.ui.modules.ModulesScreen
import com.info85.infofacil.ui.modules.ModulesViewModel
import com.info85.infofacil.ui.modules.ModulesViewModelFactory
import com.info85.infofacil.ui.onboarding.OnboardingScreen
import com.info85.infofacil.ui.progress.ProgressScreen
import com.info85.infofacil.ui.progress.ProgressViewModel
import com.info85.infofacil.ui.progress.ProgressViewModelFactory
import com.info85.infofacil.ui.quiz.QuizScreen
import com.info85.infofacil.ui.quiz.QuizViewModel
import com.info85.infofacil.ui.quiz.QuizViewModelFactory
import com.info85.infofacil.ui.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    progressRepository: ProgressRepository,
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Route.Splash.path,
    ) {
        composable(Route.Splash.path) {
            SplashScreen(
                progressRepository = progressRepository,
                onNavigateToOnboarding = {
                    navController.navigate(Route.Onboarding.path) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                    }
                },
            )
        }

        composable(Route.Onboarding.path) {
            OnboardingScreen(
                progressRepository = progressRepository,
                onComplete = {
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Onboarding.path) { inclusive = true }
                    }
                },
            )
        }

        composable(Route.Home.path) {
            val vm: HomeViewModel = viewModel(factory = HomeViewModelFactory(context.applicationContext as android.app.Application, progressRepository))
            HomeScreen(
                viewModel = vm,
                onNavigateToModules = { navController.navigate(Route.Modules.path) },
                onNavigateToModuleDetail = { moduleId ->
                    navController.navigate(Route.ModuleDetail(moduleId).buildRoute())
                },
                onNavigateToProgress = { navController.navigate(Route.Progress.path) },
            )
        }

        composable(Route.Modules.path) {
            val vm: ModulesViewModel = viewModel(factory = ModulesViewModelFactory(context.applicationContext as android.app.Application, progressRepository))
            ModulesScreen(
                viewModel = vm,
                onNavigateToModuleDetail = { moduleId ->
                    navController.navigate(Route.ModuleDetail(moduleId).buildRoute())
                },
                onNavigateToHome = { navController.navigate(Route.Home.path) },
                onNavigateToProgress = { navController.navigate(Route.Progress.path) },
            )
        }

        composable(
            route = Route.ModuleDetail.PATH,
            arguments = listOf(navArgument("moduleId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: return@composable
            val vm: ModulesViewModel = viewModel(factory = ModulesViewModelFactory(context.applicationContext as android.app.Application, progressRepository))
            ModuleDetailScreen(
                moduleId = moduleId,
                viewModel = vm,
                onNavigateToLesson = { lessonId ->
                    navController.navigate(Route.LessonRoute(moduleId, lessonId).buildRoute())
                },
                onNavigateToQuiz = {
                    navController.navigate(Route.QuizRoute(moduleId).buildRoute())
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Route.LessonRoute.PATH,
            arguments = listOf(
                navArgument("moduleId") { type = NavType.StringType },
                navArgument("lessonId") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: return@composable
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: return@composable
            val vm: LessonViewModel = viewModel(factory = LessonViewModelFactory(context.applicationContext as android.app.Application, progressRepository, moduleId, lessonId))
            LessonScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onNextLesson = { nextLessonId ->
                    navController.navigate(Route.LessonRoute(moduleId, nextLessonId).buildRoute()) {
                        popUpTo(Route.LessonRoute.PATH) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = Route.QuizRoute.PATH,
            arguments = listOf(navArgument("moduleId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: return@composable
            val vm: QuizViewModel = viewModel(factory = QuizViewModelFactory(context.applicationContext as android.app.Application, progressRepository, moduleId))
            QuizScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onFinished = { navController.popBackStack() },
            )
        }

        composable(Route.Progress.path) {
            val vm: ProgressViewModel = viewModel(factory = ProgressViewModelFactory(context.applicationContext as android.app.Application, progressRepository))
            ProgressScreen(
                viewModel = vm,
                onNavigateToHome = { navController.navigate(Route.Home.path) },
                onNavigateToModules = { navController.navigate(Route.Modules.path) },
            )
        }
    }
}
