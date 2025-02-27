package com.example.voca.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.voca.model.LessonItem


@Composable
fun PathActivity(navController: NavController) {
    var userLevel by remember { mutableStateOf(1) }
    var totalPoints by remember { mutableStateOf(0) }
    var completedLessons by remember { mutableStateOf(setOf<String>()) }

    // List of important English speaking chapters.
    val learningPath = listOf(
        LessonItem(
            title = "Pronunciation Essentials",
            id = "pronunciation",
            points = 100,
            description = "Learn correct English pronunciation for clear speech."
        ),
        LessonItem(
            title = "Conversational Fluency",
            id = "conversation",
            points = 150,
            description = "Boost your everyday conversation skills."
        ),
        LessonItem(
            title = "Accent Reduction Techniques",
            id = "accent",
            points = 200,
            description = "Neutralize your accent for better understanding."
        ),
        LessonItem(
            title = "Idiomatic Expressions",
            id = "idioms",
            points = 250,
            description = "Master common idioms to sound natural."
        ),
        LessonItem(
            title = "Public Speaking Mastery",
            id = "public_speaking",
            points = 300,
            description = "Enhance your confidence for public presentations."
        )
    )

    Scaffold(
        topBar = {
            Surface(
                elevation = 8.dp,
                color = Color(0xFF1A2C3A)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "English Speaking Chapters",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Points",
                                tint = Color.Yellow
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "$totalPoints",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        LevelBadge(userLevel)
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A2C3A),
                            Color(0xFF2C3E50)
                        )
                    )
                )
                .padding(padding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                itemsIndexed(learningPath) { index, lesson ->
                    ProfessionalLessonCard(
                        lesson = lesson,
                        isCompleted = lesson.id in completedLessons,
                        isUnlocked = index == 0 || learningPath[index - 1].id in completedLessons,
                        onLessonComplete = {
                            // Update progress and achievements.
                            completedLessons = completedLessons + lesson.id
                            totalPoints += lesson.points
                            if (totalPoints >= userLevel * 1000) {
                                userLevel++
                            }
                            // Redirect to the lesson detail screen.
                            navController.navigate("lessonDetail/${lesson.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LevelBadge(level: Int) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF27AE60))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Level $level",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfessionalLessonCard(
    lesson: LessonItem,
    isCompleted: Boolean,
    onLessonComplete: () -> Unit,
    isUnlocked: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .heightIn(min = 180.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFF253746),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.h6,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = lesson.description,
                    style = MaterialTheme.typography.body2,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Points",
                        tint = Color.Yellow,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "+${lesson.points} Points",
                        color = Color(0xFFF1C40F),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = onLessonComplete,
                enabled = isUnlocked && !isCompleted,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = when {
                        isCompleted -> Color(0xFF2ECC71)
                        !isUnlocked -> Color(0xFF7F8C8D)
                        else -> Color(0xFF3498DB)
                    }
                ),
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = when {
                        isCompleted -> "Completed"
                        !isUnlocked -> "Locked"
                        else -> "Start Chapter"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


