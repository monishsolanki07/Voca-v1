package com.example.voca.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class VideoOutput(
    val facialExpressions: String,
    val handGestureRating: Double,
    val bodyPostureRating: String,
    val overallConfidenceReport: String
)

data class AudioOutput(
    val speechRate: String,
    val fluency: String,
    val pauses: Double,
    val pitchToneVariations: String,
    val wordEmphasis: String,
    val toneAnalysis: String,
    val paceAnalysis: String,
    val clarityAnalysis: String,
    val volumeEnergyAnalysis: String
)

data class GeminiOutput(
    val grammaticalErrors: String,
    val relevancePercentage: String,
    val nonRelevantParts: String,
    val repetition: String,
    val vocabulary: String,
    val strengths: String,
    val weaknesses: String,
    val grade: String
)

class PerformanceReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoOutput = VideoOutput(
            facialExpressions = "92%",
            handGestureRating = 8.5,
            bodyPostureRating = "Excellent",
            overallConfidenceReport = "Your overall performance is outstanding. You're a natural!"
        )

        val audioOutput = AudioOutput(
            speechRate = "130.00",
            fluency = "Very Fluent",
            pauses = 1.2,
            pitchToneVariations = "High variations",
            wordEmphasis = "Strong emphasis",
            toneAnalysis = "Confident tone",
            paceAnalysis = "Fast pace",
            clarityAnalysis = "Very clear",
            volumeEnergyAnalysis = "High volume and energy"
        )

        val geminiOutput = GeminiOutput(
            grammaticalErrors = "There are no grammatical errors.",
            relevancePercentage = "60%",
            nonRelevantParts = "\"the benefits in world\"",
            repetition = "There are no repetitions.",
            vocabulary = "The speaker's vocabulary is exceptional.",
            strengths = "The speaker is clear, concise, well-organized, confident, and engaging.",
            weaknesses = "None",
            grade = "Content: 10/10"
        )

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F5F5)
                ) {
                    PerformanceReport(videoOutput, audioOutput, geminiOutput)
                }
            }
        }
    }
}

@Composable
fun PerformanceReport(
    videoOutput: VideoOutput,
    audioOutput: AudioOutput,
    geminiOutput: GeminiOutput
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Performance Report",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                backgroundColor = Color(0xFF2196F3),
                contentColor = Color.White,
                elevation = 8.dp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OverallScore(videoOutput.overallConfidenceReport)
            VideoSection(videoOutput)
            AudioSection(audioOutput)
            ContentSection(geminiOutput)
            PerformanceGraphs(videoOutput, audioOutput, geminiOutput)
        }
    }
}

@Composable
fun OverallScore(overallReport: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFF2196F3),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Overall Performance",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                overallReport,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun VideoSection(videoOutput: VideoOutput) {
    ReportCard(
        title = "Video Analysis",
        icon = Icons.Filled.Videocam
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MetricRow("Facial Expressions", videoOutput.facialExpressions)
            MetricRow("Hand Gestures", "${videoOutput.handGestureRating}/10")
            MetricRow("Body Posture", videoOutput.bodyPostureRating)

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFFE3F2FD), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = videoOutput.facialExpressions.removeSuffix("%").toFloat() / 100f,
                    modifier = Modifier.size(120.dp),
                    color = Color(0xFF2196F3),
                    strokeWidth = 12.dp
                )
                Text(
                    videoOutput.facialExpressions,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
            }
        }
    }
}

@Composable
fun AudioSection(audioOutput: AudioOutput) {
    ReportCard(
        title = "Speech Analysis",
        icon = Icons.Filled.GraphicEq
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricRow("Speech Rate", "${audioOutput.speechRate} WPM")
            MetricRow("Fluency", audioOutput.fluency)
            MetricRow("Average Pause", "${audioOutput.pauses}s")
            MetricRow("Pitch & Tone", audioOutput.pitchToneVariations)
            MetricRow("Word Emphasis", audioOutput.wordEmphasis)
            MetricRow("Tone", audioOutput.toneAnalysis)
            MetricRow("Pace", audioOutput.paceAnalysis)
            MetricRow("Clarity", audioOutput.clarityAnalysis)
            MetricRow("Volume & Energy", audioOutput.volumeEnergyAnalysis)
        }
    }
}

@Composable
fun ContentSection(geminiOutput: GeminiOutput) {
    ReportCard(
        title = "Content Analysis",
        icon = Icons.Filled.Description
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricRow("Grade", geminiOutput.grade)
            MetricRow("Relevance", geminiOutput.relevancePercentage)
            MetricRow("Grammar", "No errors")
            MetricRow("Vocabulary", "Exceptional")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Strengths",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
            Text(geminiOutput.strengths)

            if (geminiOutput.weaknesses != "None") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Areas for Improvement",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
                Text(geminiOutput.weaknesses)
            }
        }
    }
}

@Composable
fun ReportCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
            }
            content()
        }
    }
}

@Composable
fun MetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color(0xFF333333)
        )
    }
}

@Composable
fun PerformanceGraphs(
    videoOutput: VideoOutput,
    audioOutput: AudioOutput,
    geminiOutput: GeminiOutput
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Performance Metrics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircularMetric(
                    value = videoOutput.facialExpressions.removeSuffix("%").toFloat(),
                    label = "Facial\nExpressions"
                )

                CircularMetric(
                    value = (videoOutput.handGestureRating * 10).toFloat(),
                    label = "Hand\nGestures"
                )

                CircularMetric(
                    value = geminiOutput.relevancePercentage.removeSuffix("%").toFloat(),
                    label = "Content\nRelevance"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Speech Rate",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                LinearProgressIndicator(
                    progress = audioOutput.speechRate.toFloat() / 150f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = Color(0xFF2196F3),
                    backgroundColor = Color(0xFFE3F2FD)
                )
                Text(
                    "${audioOutput.speechRate} WPM",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun CircularMetric(value: Float, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFE3F2FD), CircleShape)
                .padding(8.dp)
        ) {
            CircularProgressIndicator(
                progress = value / 100f,
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF2196F3),
                strokeWidth = 8.dp
            )
            Text(
                "${value.toInt()}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
        }
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}