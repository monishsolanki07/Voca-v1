package com.example.voca.ui.theme.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReportScreen() {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feedback Report") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                elevation = 8.dp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Professional Feedback",
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                    )
                    Text(
                        text = "Performance Analysis Report",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Overall Score Card
                Card(
                    modifier = Modifier.size(80.dp),
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(16.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "6",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "/ 10",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Audio Output Section
            ExpandableSection(
                title = "Audio Output",
                icon = Icons.Default.Mic,
                iconTint = Color(0xFF5E35B1) // Deep Purple
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    RatingRow("Speech Rate (wpm)", "52.60", 0.53f)
                    RatingRow("Fluency", "Fluent", 0.85f)
                    RatingRow("Pauses", "1.917", 0.4f)
                    RatingRow("Pitch & Tone Variations", "High variations", 0.8f)
                    RatingRow("Word Emphasis", "Lacks emphasis", 0.3f)
                    RatingRow("Tone Analysis", "Positive tone", 0.9f)
                    RatingRow("Pace Analysis", "Slow pace", 0.4f)
                    RatingRow("Volume & Energy", "High volume and energy", 0.85f)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gemini Output Section
            ExpandableSection(
                title = "Content Analysis",
                icon = Icons.Default.Description,
                iconTint = Color(0xFF00897B) // Teal
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    SubsectionWithBullets(
                        title = "Grammatical Errors",
                        items = listOf(
                            "Changed from \"changed the way of education that is online education\" to \"changed the way education is delivered: online education\".",
                            "Corrected \"has travelled by now\" to \"has come a long way\".",
                            "Replaced \"learning education\" with \"learning\" or \"online education\".",
                            "Adjusted verbose internet connection sentence to a more concise version."
                        )
                    )

                    SectionMetric("Relevance", 90)

                    SubsectionWithBullets(
                        title = "Repetition",
                        items = listOf(
                            "The word \"education\" is overused; try mixing it up with synonyms."
                        )
                    )

                    SubsectionWithBullets(
                        title = "Vocabulary",
                        items = listOf(
                            "Simple and easy-to-understand vocabulary, but could be more precise and impactful."
                        )
                    )

                    SubsectionWithBullets(
                        title = "Strengths",
                        items = listOf(
                            "Clear grasp of the topic and evident enthusiasm."
                        )
                    )

                    SubsectionWithBullets(
                        title = "Weaknesses",
                        items = listOf(
                            "Needs improvement in grammar, sentence structure, and more formal tone with supporting data."
                        )
                    )

                    SubsectionWithBullets(
                        title = "Summary",
                        items = listOf(
                            "Srishti's intro shows the value of online ed, but could be stronger."
                        )
                    )

                    ScoreCard(score = 6, maxScore = 10)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Video Output Section
            ExpandableSection(
                title = "Visual Presentation",
                icon = Icons.Default.Videocam,
                iconTint = Color(0xFFE64A19) // Deep Orange
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    RatingRow("Facial Expressions", "Good", 0.87f)
                    RatingRow("Hand Gestures", "Needs Improvement", 0.2f)
                    RatingRow("Body Posture", "Average", 0.5f)

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Overall Confidence",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Shows potential; work on refining expressions, gestures, and posture.",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Share Report")
                }

                Spacer(modifier = Modifier.width(16.dp))

                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Download PDF")
                }
            }
        }
    }
}

@Composable
fun ExpandableSection(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        elevation = 3.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon with background
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            // Divider
            Divider()

            // Content
            if (expanded) {
                Box(modifier = Modifier.padding(16.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
fun RatingRow(label: String, value: String, filledPortion: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.body2
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(filledPortion)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        when {
                            filledPortion < 0.4f -> Color(0xFFE57373) // Red-ish
                            filledPortion < 0.7f -> Color(0xFFFFB74D) // Orange-ish
                            else -> Color(0xFF81C784) // Green-ish
                        }
                    )
            )
        }
    }
}

@Composable
fun SubsectionWithBullets(title: String, items: List<String>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        items.forEach { item ->
            Row(
                modifier = Modifier.padding(vertical = 2.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "•",
                    modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                )
                Text(text = item)
            }
        }
    }
}

@Composable
fun SectionMetric(label: String, percentage: Int) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            // Progress circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        when {
                            percentage < 40 -> Color(0xFFE57373) // Red-ish
                            percentage < 70 -> Color(0xFFFFB74D) // Orange-ish
                            else -> Color(0xFF81C784) // Green-ish
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$percentage%",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Some phrases could use more structure and impact.",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun ScoreCard(score: Int, maxScore: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Card(
            elevation = 0.dp,
            backgroundColor = when {
                score < 4 -> Color(0xFFFFEBEE) // Light Red
                score < 7 -> Color(0xFFFFF3E0) // Light Orange
                else -> Color(0xFFE8F5E9) // Light Green
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Overall Grade",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            when {
                                score < 4 -> Color(0xFFE57373) // Red
                                score < 7 -> Color(0xFFFFB74D) // Orange
                                else -> Color(0xFF81C784) // Green
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$score",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "/ $maxScore",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}