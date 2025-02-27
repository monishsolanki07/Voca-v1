package com.example.voca.ui.theme.screen

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.voca.R

@Composable
fun ProfileScreen () {
    val primaryColor = Color(0xFF6A5ACD)
    val secondaryColor = Color(0xFFA78FEA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                backgroundColor = primaryColor,
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // User Profile Header
            item {
                UserProfileHeader(
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor
                )
            }

            // Performance Metrics
            item {
                PerformanceMetrics(
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor
                )
            }

            // Skill Progress
            item {
                SkillProgressSection(
                    primaryColor = primaryColor
                )
            }

            // Achievements
            item {
                AchievementsSection(
                    primaryColor = primaryColor
                )
            }
        }
    }
}

@Composable
fun UserProfileHeader(
    primaryColor: Color,
    secondaryColor: Color
) {
    // Retrieve stored username; default to "Admin" if not found.
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val storedUsername = sharedPref.getString("username", "Admin")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(primaryColor, secondaryColor)
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.a1),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Use the stored username instead of the hardcoded "Admin".
            Text(
                text = storedUsername ?: "Admin",
                color = Color.White,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Language Learning Enthusiast",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

@Composable
fun PerformanceMetrics(
    primaryColor: Color,
    secondaryColor: Color
) {
    // State variables for performance metrics, initially set to zero.
    var studyHours by remember { mutableStateOf(0) }
    var completion by remember { mutableStateOf(0) }  // Percentage (0%)
    var dayStreak by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Performance Overview",
                style = MaterialTheme.typography.h6,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                PerformanceItem(
                    icon = Icons.Default.AccessTime,
                    value = studyHours.toString(),
                    label = "Study Hours",
                    color = primaryColor
                )
                PerformanceItem(
                    icon = Icons.Default.CheckCircle,
                    value = "$completion%",
                    label = "Completion",
                    color = primaryColor
                )
                PerformanceItem(
                    icon = Icons.Default.Star,
                    value = dayStreak.toString(),
                    label = "Day Streak",
                    color = primaryColor
                )
            }
        }
    }
}

@Composable
fun PerformanceItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )
    }
}

@Composable
fun SkillProgressSection(primaryColor: Color) {
    // Skill progress initially set to 0%.
    var progress by remember { mutableStateOf(0.0f) }
    val animatedProgress by animateFloatAsState(targetValue = progress)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Skill Progress",
                style = MaterialTheme.typography.h6,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Background circle
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.2f),
                        style = Stroke(width = 20f)
                    )

                    // Progress arc
                    drawArc(
                        color = primaryColor,
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = 20f, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                        color = primaryColor
                    )
                    Text(
                        text = "Speaking Skills",
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementsSection(primaryColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Achievements",
                style = MaterialTheme.typography.h6,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // All achievements initially at 0 progress.
            val achievements = listOf(
                "Vocabulary Master" to 0.0f,
                "Conversation Skills" to 0.0f,
                "Pronunciation Expert" to 0.0f
            )
            achievements.forEach { (title, progressValue) ->
                AchievementItem(
                    title = title,
                    progress = progressValue,
                    primaryColor = primaryColor
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun AchievementItem(
    title: String,
    progress: Float,
    primaryColor: Color
) {
    val animatedProgress by animateFloatAsState(targetValue = progress)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1
        )

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(10.dp)
                .background(primaryColor.copy(alpha = 0.2f), shape = RoundedCornerShape(5.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .background(primaryColor, shape = RoundedCornerShape(5.dp))
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )
    }
}
