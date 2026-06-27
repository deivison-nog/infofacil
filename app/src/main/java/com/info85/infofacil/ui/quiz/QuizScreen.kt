package com.info85.infofacil.ui.quiz

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.info85.infofacil.ui.theme.GreenAccent

private val optionLetters = listOf("A", "B", "C", "D")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit,
    onFinished: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Quiz", fontWeight = FontWeight.Bold)
                        Text(
                            text = state.moduleTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
            )
        },
    ) { paddingValues ->
        if (state.isFinished) {
            QuizResultScreen(
                score = state.score,
                total = state.questions.size,
                moduleTitle = state.moduleTitle,
                onRetry = { viewModel.restartQuiz() },
                onBack = onFinished,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        } else {
            val question = state.questions.getOrNull(state.currentIndex) ?: return@Scaffold
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(
                            text = "Pergunta ${state.currentIndex + 1} de ${state.questions.size}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { (state.currentIndex + 1).toFloat() / state.questions.size },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = question.question,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 30.sp,
                        )
                    }
                }

                itemsIndexed(question.options) { index, option ->
                    val isSelected = state.selectedAnswer == index
                    val isCorrect = question.correctIndex == index
                    val isWrong = isSelected && !isCorrect

                    val containerColor by animateColorAsState(
                        targetValue = when {
                            !state.isAnswered -> MaterialTheme.colorScheme.surface
                            isCorrect -> Color(0xFFDCFCE7)
                            isWrong -> Color(0xFFFEE2E2)
                            else -> MaterialTheme.colorScheme.surface
                        },
                        label = "optionColor",
                    )
                    val borderColor = when {
                        !state.isAnswered -> MaterialTheme.colorScheme.outlineVariant
                        isCorrect -> GreenAccent
                        isWrong -> Color(0xFFEF4444)
                        else -> MaterialTheme.colorScheme.outlineVariant
                    }

                    OutlinedCard(
                        onClick = { if (!state.isAnswered) viewModel.answerQuestion(index) },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
                        border = BorderStroke(
                            width = if (isSelected || (state.isAnswered && isCorrect)) 2.dp else 1.dp,
                            color = borderColor,
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = when {
                                        !state.isAnswered -> MaterialTheme.colorScheme.primaryContainer
                                        isCorrect -> GreenAccent
                                        isWrong -> Color(0xFFEF4444)
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                ),
                                modifier = Modifier.size(36.dp),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = optionLetters[index],
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = when {
                                            !state.isAnswered -> MaterialTheme.colorScheme.onPrimaryContainer
                                            isCorrect || isWrong -> Color.White
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.size(12.dp))
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                if (state.isAnswered) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF0FDF4),
                            ),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (state.selectedAnswer == question.correctIndex) "✅ Correto!" else "❌ Incorreto",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.selectedAnswer == question.correctIndex) GreenAccent else Color(0xFFDC2626),
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = question.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    lineHeight = 22.sp,
                                )
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                if (state.currentIndex + 1 >= state.questions.size) {
                                    viewModel.finishQuiz()
                                } else {
                                    viewModel.nextQuestion()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Text(
                                text = if (state.currentIndex + 1 >= state.questions.size) "Ver resultado" else "Próxima pergunta →",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizResultScreen(
    score: Int,
    total: Int,
    moduleTitle: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val percentage = if (total > 0) (score.toFloat() / total * 100).toInt() else 0
    val isPassed = percentage >= 60

    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = if (isPassed) "🎉" else "📚",
            fontSize = 72.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isPassed) "Parabéns!" else "Continue praticando!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isPassed) "Você demonstrou bom conhecimento sobre $moduleTitle."
            else "Revise as aulas e tente novamente. Você consegue!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isPassed) Color(0xFFDCFCE7) else Color(0xFFFEF2F2),
            ),
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "$score/$total",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isPassed) GreenAccent else Color(0xFFDC2626),
                )
                Text(
                    text = "$percentage% de acerto",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text("Voltar ao módulo", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
        ) {
            Text("Tentar novamente", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
