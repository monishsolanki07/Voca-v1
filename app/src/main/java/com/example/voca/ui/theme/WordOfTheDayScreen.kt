package com.example.voca.ui.theme

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun WordOfTheDayScreen() {
    val primaryColor = Color(0xFF6A1B9A)
    val secondaryColor = Color(0xFF81D4FA)

    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var isQuizCompleted by remember { mutableStateOf(false) }
    var showPreviousWordDialog by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Pronunciation function
    fun pronounceWord(word: String) {
        try {
            val mediaPlayer = MediaPlayer.create(
                context,
                Uri.parse("android.resource://${context.packageName}/raw/pronunciation")
            )
            mediaPlayer?.start()
        } catch (e: Exception) {
            // Handle pronunciation error
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Word of the Day", fontWeight = FontWeight.Bold) },
                backgroundColor = primaryColor,
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF0F4F8))
                .padding(16.dp)
        ) {
            // Word of the Day Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Articulate",
                            style = MaterialTheme.typography.h4.copy(
                                fontWeight = FontWeight.Bold,
                                color = primaryColor
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { pronounceWord("Articulate") }) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Pronounce",
                                tint = primaryColor
                            )
                        }
                    }
                    Text(
                        text = "/ˈɑːr.tɪ.kjə.leɪt/",
                        style = MaterialTheme.typography.subtitle1,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "To express oneself clearly and effectively in words.",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "\"She was able to articulate her complex ideas with remarkable clarity.\"",
                        style = MaterialTheme.typography.body2.copy(
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        ),
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Previous Words
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Previous Words",
                style = MaterialTheme.typography.h6,
                color = primaryColor
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PreviousWordChip("Serendipity", primaryColor) {
                    showPreviousWordDialog = "Serendipity"
                }
                PreviousWordChip("Ephemeral", secondaryColor) {
                    showPreviousWordDialog = "Ephemeral"
                }
            }

            // Quiz Section
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Word Quiz",
                style = MaterialTheme.typography.h6,
                color = primaryColor
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val quizQuestions = listOf(
                        QuizQuestion(
                            question = "What does 'Articulate' mean?",
                            options = listOf(
                                "To speak unclearly",
                                "To express thoughts clearly",
                                "To remain silent",
                                "To argue loudly"
                            ),
                            correctAnswer = 1
                        ),
                        QuizQuestion(
                            question = "In what part of speech is 'Articulate' used here?",
                            options = listOf(
                                "Noun",
                                "Verb",
                                "Adjective",
                                "Adverb"
                            ),
                            correctAnswer = 1
                        )
                    )

                    var currentQuestionIndex by remember { mutableStateOf(0) }
                    val currentQuestion = quizQuestions[currentQuestionIndex]

                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    currentQuestion.options.forEachIndexed { index, option ->
                        QuizOption(
                            text = option,
                            index = index,
                            selectedAnswer = selectedAnswer,
                            isQuizCompleted = isQuizCompleted,
                            onSelect = {
                                selectedAnswer = index
                                isQuizCompleted = true
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (isQuizCompleted) {
                        val isCorrect = selectedAnswer == currentQuestion.correctAnswer
                        Text(
                            text = if (isCorrect) "Correct! 🎉" else "Incorrect. Try again! 🤔",
                            color = if (isCorrect) Color.Green else Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Button(
                            onClick = {
                                if (currentQuestionIndex < quizQuestions.size - 1) {
                                    currentQuestionIndex++
                                    selectedAnswer = null
                                    isQuizCompleted = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text("Next Question", color = Color.White)
                        }
                    }
                }
            }
        }

        // Previous Word Dialog
        if (showPreviousWordDialog != null) {
            AlertDialog(
                onDismissRequest = { showPreviousWordDialog = null },
                title = {
                    Text(
                        text = showPreviousWordDialog!!,
                        color = primaryColor,
                        style = MaterialTheme.typography.h6
                    )
                },
                text = {
                    Column {
                        Text(
                            text = when(showPreviousWordDialog) {
                                "Serendipity" -> "The occurrence of events by chance in a happy or beneficial way."
                                "Ephemeral" -> "Lasting for a very short time; transitory or temporary."
                                else -> ""
                            }
                        )
                        Text(
                            text = "Example: " + when(showPreviousWordDialog) {
                                "Serendipity" -> "Finding a great restaurant by accident is a perfect example of serendipity."
                                "Ephemeral" -> "Social media trends are often ephemeral, lasting only a few days."
                                else -> ""
                            },
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPreviousWordDialog = null }) {
                        Text("Close", color = primaryColor)
                    }
                }
            )
        }
    }
}

@Composable
fun QuizOption(
    text: String,
    index: Int,
    selectedAnswer: Int?,
    isQuizCompleted: Boolean,
    onSelect: () -> Unit
) {
    val primaryColor = Color(0xFF6A1B9A)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isQuizCompleted) { onSelect() }
            .background(
                color = when {
                    isQuizCompleted && index == 1 -> Color.Green.copy(alpha = 0.2f)
                    isQuizCompleted && selectedAnswer == index && selectedAnswer != 1 -> Color.Red.copy(alpha = 0.2f)
                    selectedAnswer == index -> primaryColor.copy(alpha = 0.2f)
                    else -> Color.LightGray.copy(alpha = 0.1f)
                }
            )
            .padding(12.dp)
    ) {
        Text(
            text = text,
            color = when {
                isQuizCompleted && index == 1 -> Color.Green
                isQuizCompleted && selectedAnswer == index && selectedAnswer != 1 -> Color.Red
                selectedAnswer == index -> primaryColor
                else -> Color.Black
            }
        )
    }
}

@Composable
fun PreviousWordChip(word: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = word,
            color = color,
            style = MaterialTheme.typography.caption
        )
    }
}

// Data class for Quiz Questions
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)