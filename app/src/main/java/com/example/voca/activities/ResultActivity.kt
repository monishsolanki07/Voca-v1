package com.example.voca.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName

// --- Data Classes Matching Your JSON Format Exactly ---
data class VideoOutput(
    @SerializedName("Facial Expressions (Percentage)")
    val facialExpressions: String,
    @SerializedName("Hand Gesture Rating (out of 10)")
    val handGestureRating: Double,
    @SerializedName("Body Posture Rating")
    val bodyPostureRating: String,
    @SerializedName("Overall Confidence Report")
    val overallConfidenceReport: String
)

data class AudioOutput(
    @SerializedName("Speech Rate (words per minute)")
    val speechRate: String,
    @SerializedName("Fluency")
    val fluency: String,
    @SerializedName("Pauses")
    val pauses: Double,
    @SerializedName("Pitch and Tone Variations")
    val pitchToneVariations: String,
    @SerializedName("Word Emphasis")
    val wordEmphasis: String,
    @SerializedName("Tone Analysis")
    val toneAnalysis: String,
    @SerializedName("Pace Analysis")
    val paceAnalysis: String,
    @SerializedName("Clarity Analysis")
    val clarityAnalysis: String,
    @SerializedName("Volume and Energy Analysis")
    val volumeEnergyAnalysis: String
)

// ReportResult now uses gemini_output as String.
data class ReportResult(
    val video_output: VideoOutput,
    val audio_output: AudioOutput,
    val gemini_output: String
)

// --- Retrofit API Interface for /videojson/ ---
interface ReportApiService {
    @GET("videojson/")
    fun getReportResult(): Call<ReportResult>
}

// --- ResultActivity with Long Polling ---
class ResultActivity : ComponentActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var reportApiService: ReportApiService
    private val pollingIntervalMillis: Long = 2000 // Poll every 2 seconds
    private lateinit var pollingHandler: Handler
    private lateinit var pollingRunnable: Runnable

    // Mutable state to hold the report result; initially null.
    private var reportResultState by mutableStateOf<ReportResult?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Retrofit with your server's base URL.
        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.42.175:8000/") // Update this with your server IP/port.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        reportApiService = retrofit.create(ReportApiService::class.java)
        pollingHandler = Handler(Looper.getMainLooper())

        setContent {
            ReportScreen(reportResult = reportResultState)
        }

        startPolling()
    }

    private fun startPolling() {
        pollingRunnable = object : Runnable {
            override fun run() {
                pollForReport()
                pollingHandler.postDelayed(this, pollingIntervalMillis)
            }
        }
        pollingHandler.post(pollingRunnable)
    }

    private fun stopPolling() {
        pollingHandler.removeCallbacks(pollingRunnable)
    }

    private fun pollForReport() {
        reportApiService.getReportResult().enqueue(object : Callback<ReportResult> {
            override fun onResponse(call: Call<ReportResult>, response: Response<ReportResult>) {
                if (response.isSuccessful && response.body() != null) {
                    stopPolling()
                    // Update state on the main thread.
                    runOnUiThread {
                        reportResultState = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<ReportResult>, t: Throwable) {
                t.printStackTrace()
                // Continue polling even if an error occurs.
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPolling()
    }
}

// --- Compose UI to Render the Report ---
@Composable
fun ReportScreen(reportResult: ReportResult?) {
    if (reportResult == null) {
        // Show a loading indicator while waiting for the JSON.
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Waiting for analysis result...")
            }
        }
    } else {
        // Once the JSON arrives, display the detailed report.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { OverallScore(reportResult.video_output.overallConfidenceReport) }
            item { VideoSection(reportResult.video_output) }
            item { AudioSection(reportResult.audio_output) }
            item { ContentSection(reportResult.gemini_output) }
            item { PerformanceGraphs(reportResult) }
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
            MetricRow("Pauses", "${audioOutput.pauses}s")
            MetricRow("Pitch & Tone", audioOutput.pitchToneVariations)
            MetricRow("Word Emphasis", audioOutput.wordEmphasis)
            MetricRow("Tone Analysis", audioOutput.toneAnalysis)
            MetricRow("Pace Analysis", audioOutput.paceAnalysis)
            MetricRow("Clarity Analysis", audioOutput.clarityAnalysis)
            MetricRow("Volume & Energy", audioOutput.volumeEnergyAnalysis)
        }
    }
}

@Composable
fun ContentSection(geminiOutput: String) {
    ReportCard(
        title = "Detailed Feedback",
        icon = Icons.Filled.Description
    ) {
        Text(text = geminiOutput, style = typography.body2)
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
        Column(modifier = Modifier.padding(16.dp)) {
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
fun PerformanceGraphs(reportResult: ReportResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    value = reportResult.video_output.facialExpressions.removeSuffix("%").toFloat(),
                    label = "Facial\nExpressions"
                )
                CircularMetric(
                    value = (reportResult.video_output.handGestureRating * 10).toFloat(),
                    label = "Hand\nGestures"
                )
                // Extract a numeric value from gemini_output for content relevance.
                val relevance = reportResult.gemini_output.substringAfter("Relevance percentage: ")
                    .substringBefore("%")
                    .toFloatOrNull() ?: 0f
                CircularMetric(
                    value = relevance,
                    label = "Content\nRelevance"
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Additional graphs or metrics can be added here.
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
