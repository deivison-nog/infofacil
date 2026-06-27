package com.info85.infofacil.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.info85.infofacil.model.Module
import com.info85.infofacil.model.UserProgress
import com.info85.infofacil.model.progressFraction
import com.info85.infofacil.ui.components.BottomNavBar
import com.info85.infofacil.ui.components.BottomNavTab
import com.info85.infofacil.ui.components.toColor
import com.info85.infofacil.ui.components.toGradientColors
import com.info85.infofacil.ui.theme.BluePrimary
import com.info85.infofacil.ui.theme.BlueSecondary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToModules: () -> Unit,
    onNavigateToModuleDetail: (String) -> Unit,
    onNavigateToProgress: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab = BottomNavTab.Home,
                onTabSelected = { tab ->
                    when (tab) {
                        BottomNavTab.Modules -> onNavigateToModules()
                        BottomNavTab.Progress -> onNavigateToProgress()
                        BottomNavTab.Home -> Unit
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item {
                HomeHeader(
                    totalModulesStarted = state.totalModulesStarted,
                    totalModules = state.modules.size,
                    totalLessonsCompleted = state.totalLessonsCompleted,
                )
            }

            item {
                if (state.lastStartedModuleId != null) {
                    val lastModule = state.modules.firstOrNull { it.id == state.lastStartedModuleId }
                    val progress = state.progress[state.lastStartedModuleId]
                    if (lastModule != null && progress != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = "Continuar aprendendo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ContinueModuleCard(
                                module = lastModule,
                                progress = progress,
                                onClick = { onNavigateToModuleDetail(lastModule.id) },
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Módulos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Ver todos →",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onNavigateToModules() },
                    )
                }
            }

            items(state.modules.take(4)) { module ->
                val progress = state.progress[module.id]
                Spacer(modifier = Modifier.height(8.dp))
                ModuleCard(
                    module = module,
                    progress = progress,
                    onClick = { onNavigateToModuleDetail(module.id) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                StatsRow(
                    totalLessons = state.totalLessonsCompleted,
                    totalModules = state.totalModulesCompleted,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(
    totalModulesStarted: Int,
    totalModules: Int,
    totalLessonsCompleted: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(BluePrimary, BlueSecondary)))
            .padding(horizontal = 20.dp, vertical = 28.dp),
    ) {
        Column {
            Text(
                text = "Olá 👋",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f),
            )
            Text(
                text = "InfoFácil",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = "Informática para todos",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.15f),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "$totalModulesStarted de $totalModules módulos em andamento",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { if (totalModules > 0) totalModulesStarted.toFloat() / totalModules else 0f },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$totalLessonsCompleted aulas concluídas",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                }
            }
        }
    }
}

@Composable
private fun ContinueModuleCard(
    module: Module,
    progress: UserProgress,
    onClick: () -> Unit,
) {
    val accentColor = module.accent.toColor()
    val gradientColors = module.accent.toGradientColors()
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(gradientColors))
                .padding(20.dp),
        ) {
            Column {
                Text(
                    text = module.iconEmoji,
                    fontSize = 36.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Text(
                    text = "${progress.completedLessonIds.size} de ${module.lessons.size} aulas • ${module.duration}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f),
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { progress.progressFraction },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.3f),
                )
            }
        }
    }
}

@Composable
fun ModuleCard(
    module: Module,
    progress: UserProgress?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor = module.accent.toColor()
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(module.iconEmoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "${module.lessons.size} aulas • ${module.duration}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (progress != null && progress.completedLessonIds.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress.progressFraction },
                        modifier = Modifier.fillMaxWidth(),
                        color = accentColor,
                        trackColor = accentColor.copy(alpha = 0.2f),
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun StatsRow(
    totalLessons: Int,
    totalModules: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            emoji = "📖",
            value = totalLessons.toString(),
            label = "Aulas concluídas",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            emoji = "🏆",
            value = totalModules.toString(),
            label = "Módulos concluídos",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatCard(
    emoji: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
