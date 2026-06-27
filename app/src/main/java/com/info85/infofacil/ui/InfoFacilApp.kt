package com.info85.infofacil.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.info85.infofacil.data.LocalModulesDataSource
import com.info85.infofacil.model.Module
import com.info85.infofacil.model.ModuleAccent

@Composable
fun InfoFacilApp() {
    InfoFacilHomeScreen(modules = LocalModulesDataSource.modules)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoFacilHomeScreen(modules: List<Module>) {
    var selectedModule by remember { mutableStateOf(modules.first()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "InfoFácil",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Informática para todos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                HeroCard(selectedModule)
            }

            item {
                SectionTitle(title = "Módulos offline")
            }

            items(modules) { module ->
                ModuleCard(
                    module = module,
                    onStartClick = { selectedModule = module },
                )
            }

            item {
                SectionTitle(title = "Como você vai aprender")
            }

            item {
                LearningExperienceCard()
            }
        }
    }
}

@Composable
private fun HeroCard(module: Module) {
    val colors = module.accent.colors()
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(colors))
                .padding(24.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Continue aprendendo",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.92f),
                )
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.95f),
                )
                LinearProgressIndicator(
                    progress = { module.progress },
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "${(module.progress * 100).toInt()}% concluído • ${module.lessons} aulas • ${module.duration}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                )
                Button(onClick = { }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Continuar")
                }
            }
        }
    }
}

@Composable
private fun ModuleCard(
    module: Module,
    onStartClick: () -> Unit,
) {
    val accent = module.accent.contentColor()
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = module.icon(),
                    contentDescription = null,
                    tint = accent,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "${module.lessons} aulas • ${module.duration}",
                    style = MaterialTheme.typography.labelMedium,
                    color = accent,
                )
            }
            Button(onClick = onStartClick) {
                Text("Abrir")
            }
        }
    }
}

@Composable
private fun LearningExperienceCard() {
    Surface(
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Conteúdo pensado para iniciantes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Aulas curtas, ilustrações, prática guiada e progresso salvo no aparelho sem necessidade de login.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "• Texto simples\n• Elementos visuais\n• Exercícios rápidos\n• Funciona offline",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
}

private fun ModuleAccent.colors(): List<Color> = when (this) {
    ModuleAccent.Blue -> listOf(Color(0xFF2563EB), Color(0xFF0EA5E9))
    ModuleAccent.Green -> listOf(Color(0xFF16A34A), Color(0xFF22C55E))
    ModuleAccent.Orange -> listOf(Color(0xFFF97316), Color(0xFFF59E0B))
    ModuleAccent.Purple -> listOf(Color(0xFF7C3AED), Color(0xFFA855F7))
}

@Composable
private fun ModuleAccent.contentColor(): Color = when (this) {
    ModuleAccent.Blue -> Color(0xFF2563EB)
    ModuleAccent.Green -> Color(0xFF16A34A)
    ModuleAccent.Orange -> Color(0xFFF97316)
    ModuleAccent.Purple -> Color(0xFF7C3AED)
}

@Composable
private fun Module.icon() = when (accent) {
    ModuleAccent.Blue -> Icons.Default.School
    ModuleAccent.Green -> Icons.Default.TipsAndUpdates
    ModuleAccent.Orange -> Icons.Default.PlayArrow
    ModuleAccent.Purple -> Icons.Default.Security
}
