package com.example.voca.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.voca.R
import com.example.voca.activities.ResultActivity
import com.example.voca.viewmodel.RegisterViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data classes for Retrofit
data class VideoAnalysisRequest(
    val verificationHash: String,
    val reportID: String,
    val activityName: String,
    val videoID: String,
    val videoLink: String
)

data class VideoAnalysisResponse(
    val success: Boolean,
    val message: String?
)

// Retrofit API interface
interface ApiService {
    @POST("api/videoanalysis")
    fun sendVideoAnalysis(@Body request: VideoAnalysisRequest): Call<VideoAnalysisResponse>
}

class PreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoPath = intent.getStringExtra("VIDEO_PATH")
        val topicName = intent.getStringExtra("TOPIC_NAME") ?: "Unknown Topic"

        if (videoPath.isNullOrEmpty()) {
            Toast.makeText(this, "Video not found!", Toast.LENGTH_SHORT).show()
            finish()
        }

        setContent {
            PreviewScreen(videoPath!!, topicName)
        }
    }
}

@Composable
fun PreviewScreen(videoPath: String, topicName: String) {
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel()

    // States for upload status
    var isUploading by remember { mutableStateOf(false) }
    var uploadMessage by remember { mutableStateOf("") }

    // Initialize ExoPlayer for video playback
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.fromFile(File(videoPath))))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Preview - $topicName") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Video Player occupies 75% of the screen height
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = true
                        this.player = player
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Row with Download and Upload buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Download Button (unchanged)
                Button(
                    onClick = { downloadVideo(context, videoPath) },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = "Download Video")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Download")
                }

                // Upload Button: after Firebase upload, send JSON to server via Retrofit
                Button(
                    onClick = {
                        isUploading = true
                        val videoUri = Uri.fromFile(File(videoPath))
                        viewModel.uploadVideoFirebase(topicName, videoUri,
                            onUploadSuccess = { firebaseUrl ->
                                // Generate new videoID based on topicName and current datetime
                                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                                val newVideoID = "${topicName}_$timestamp"

                                // Build request payload
                                val requestPayload = VideoAnalysisRequest(
                                    verificationHash = "8214fb8d89789cb42c3aaa797d92db4865b696d01ea36f93835f2208a8f5fbb2760376d0fc8653f4d68b5a49e0cdb263f418529c028970eb1385d951197005e8",
                                    reportID = "83921234",
                                    activityName = topicName,
                                    videoID = newVideoID,
                                    videoLink = firebaseUrl
                                )

                                // Create Retrofit instance with your server's IP address and port
                                val retrofit = Retrofit.Builder()
                                    .baseUrl("http://172.25.49.19:8000/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()

                                val apiService = retrofit.create(ApiService::class.java)
                                apiService.sendVideoAnalysis(requestPayload).enqueue(object : Callback<VideoAnalysisResponse> {
                                    override fun onResponse(call: Call<VideoAnalysisResponse>, response: Response<VideoAnalysisResponse>) {
                                        Handler(Looper.getMainLooper()).post {
                                            if (response.isSuccessful) {
                                                Toast.makeText(context, "Server received analysis", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Server error: ${response.code()}", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<VideoAnalysisResponse>, t: Throwable) {
                                        t.printStackTrace()
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(context, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                })

                                // Redirect to ResultActivity with the Firebase download URL
                                val intent = Intent(context, ResultActivity::class.java)
                                intent.putExtra("DOWNLOAD_URL", firebaseUrl)
                                context.startActivity(intent)
                                isUploading = false
                            },
                            onUploadFailure = { error ->
                                uploadMessage = "Firebase upload failed: $error"
                                isUploading = false
                            }
                        )
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Upload, contentDescription = "Upload Video")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Upload")
                }
            }

            // Show a progress indicator while uploading
            if (isUploading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            // Show upload status message
            if (uploadMessage.isNotEmpty()) {
                Text(text = uploadMessage, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

// Function to handle video download (unchanged)
private fun downloadVideo(context: Context, videoPath: String) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Video.Media.DISPLAY_NAME, File(videoPath).name)
        put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Voca")
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            File(videoPath).inputStream().use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        Toast.makeText(context, "Video saved in Gallery!", Toast.LENGTH_SHORT).show()
    } ?: Toast.makeText(context, "Failed to save video!", Toast.LENGTH_SHORT).show()
}
