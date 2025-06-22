package com.example.voca.activities

import com.example.voca.ui.theme.VocaTheme
import com.example.voca.R

import com.example.voca.activities.CameraActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

// Custom Colors
private val primaryColor = Color(0xFF9C7FE4)
private val primaryVariant = Color(0xFF7E57C2)
private val secondaryColor = Color(0xFFD1A4E8)
private val accentColor = Color(0xFF6A1B9A)
private val lightPurple = Color(0xFFD1C4E9)
private val veryLightPurple = Color(0xFFF3E5F5)

// Simplified Data Model
data class ActivityItem(
    val title: String,
    val keywords: String,
    val difficulty: String,
    val xpPoints: Int,
    val imageResId: Int = R.drawable.a2
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
            ActivityItem("Time Management", "Planning, Productivity, Prioritization", "Beginner", 100),
            ActivityItem("Social Media: Boon or Bane?", "Impact, Usage, Ethics", "Intermediate", 150),
            ActivityItem("Why is Failure Important for Success", "Resilience, Learning, Growth", "Advanced", 200),
            ActivityItem("The Role of Human Humour in Daily Life", "Laughter, Stress Relief, Social Bonding", "Beginner", 100),
            ActivityItem("Degree Equals Success?", "Education, Career, Skillset", "Intermediate", 150),
            ActivityItem("Self Confidence", "Belief, Assertiveness, Growth Mindset", "Advanced", 200),
            ActivityItem("Solo Travelling", "Adventure, Independence, Exploration", "Expert", 250),
            ActivityItem("Internet and Human Interactions", "Communication, Digital Age, Social Impact", "Expert", 300),
            ActivityItem("AI: Boon or Bane", "Technology, Ethics, Future", "Advanced", 200),
            ActivityItem("Lonely vs Alone", "Independence, Mental Health, Self-Discovery", "Intermediate", 150)
        )

    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = primaryVariant,
                elevation = 4.dp
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
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        ),
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(primaryColor.copy(alpha = 0.5f))
                            .padding(8.dp)
                    ) {
                        IconButton(
                            onClick = { /* Profile action */ },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Profile",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        },
        backgroundColor = veryLightPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Speaking Activities",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                color = accentColor
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(activities) { activity ->
                    ActivityCardWithImage(activity) {
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
}

@Composable
fun ActivityCardWithImage(activity: ActivityItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        backgroundColor = Color.White,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Image Section - Takes up top 60% of card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
            ) {
                Image(
                    painter = painterResource(id = activity.imageResId),
                    contentDescription = activity.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Difficulty chip overlaid on image
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                ) {
                    DifficultyChip(activity.difficulty)
                }
            }

            // Content Section - Takes up bottom 40% of card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = activity.title,
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = primaryVariant
                    )

                    Text(
                        text = activity.keywords,
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(lightPurple)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "XP",
                        tint = accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${activity.xpPoints} XP",
                        style = MaterialTheme.typography.caption,
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.ArrowForward,
                        contentDescription = "Start Activity",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DifficultyChip(difficulty: String) {
    val (backgroundColor, textColor) = when (difficulty) {
        "Beginner" -> Color(0xFFDCEDC8).copy(alpha = 0.9f) to Color(0xFF33691E)
        "Intermediate" -> Color(0xFFFFECB3).copy(alpha = 0.9f) to Color(0xFFE65100)
        "Advanced" -> Color(0xFFBBDEFB).copy(alpha = 0.9f) to Color(0xFF0D47A1)
        else -> secondaryColor.copy(alpha = 0.9f) to accentColor
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = difficulty,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
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