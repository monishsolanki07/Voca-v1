package com.example.voca.ui.theme

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

// Simplified Data Model
data class ActivityItem(
    val title: String,
    val keywords: String,
    val difficulty: String,
    val xpPoints: Int
)

class ActivityListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VocaTheme {
                ActivityListScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ActivityListScreen() {
    val context = LocalContext.current
    val activities = remember {
        listOf(
            ActivityItem("Public Speaking Basics", "Confidence, Clarity, Tone", "Beginner", 100),
            ActivityItem("Presentation Skills", "Slides, Engagement, Pace", "Intermediate", 150),
            ActivityItem("Debate Practice", "Arguments, Rebuttals, Evidence", "Advanced", 200),
            ActivityItem("Storytelling", "Narrative, Emotions, Impact", "Beginner", 100),
            ActivityItem("Interview Skills", "Questions, Answers, Body Language", "Intermediate", 150),
            ActivityItem("Group Discussion", "Leadership, Teamwork, Communication", "Advanced", 200),
            ActivityItem("Impromptu Speaking", "Quick Thinking, Adaptability", "Expert", 250),
            ActivityItem("Professional Pitching", "Persuasion, Business", "Expert", 300)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "VOCA",
                        style = MaterialTheme.typography.h5.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    IconButton(onClick = { /* Profile action */ }) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(activities) { activity ->
                ActivityCard(activity) {
                    val intent = Intent(context, CameraActivity::class.java).apply {
                        putExtra("TOPIC_NAME", activity.title)
                        putExtra("KEYWORDS", activity.keywords)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun ActivityCard(activity: ActivityItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                XPBadge(activity.xpPoints)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = activity.keywords,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            DifficultyChip(activity.difficulty)
        }
    }
}

@Composable
fun XPBadge(xp: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            Icons.Filled.Star,
            contentDescription = "XP",
            tint = MaterialTheme.colors.secondary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "$xp XP",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.secondary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DifficultyChip(difficulty: String) {
    val backgroundColor = when (difficulty) {
        "Beginner" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
        "Intermediate" -> Color(0xFFFFA000).copy(alpha = 0.1f)
        "Advanced" -> Color(0xFF2196F3).copy(alpha = 0.1f)
        else -> Color(0xFF9C27B0).copy(alpha = 0.1f)
    }

    val textColor = when (difficulty) {
        "Beginner" -> Color(0xFF4CAF50)
        "Intermediate" -> Color(0xFFFFA000)
        "Advanced" -> Color(0xFF2196F3)
        else -> Color(0xFF9C27B0)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = difficulty,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.caption,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
fun PreviewActivityListScreen() {
    VocaTheme {
        ActivityListScreen()
    }
}