package com.example.voca.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.voca.ui.theme.VocaTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        val topicName = intent.getStringExtra("TOPIC_NAME") ?: "Unknown Topic"
        val keywords = intent.getStringExtra("KEYWORDS") ?: "No Keywords"

        requestPermissions(topicName, keywords)
    }

    private fun requestPermissions(topicName: String, keywords: String) {
        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true &&
                permissions[Manifest.permission.RECORD_AUDIO] == true) {
                setContent {
                    VocaTheme {
                        CameraScreen(topicName, keywords)
                    }
                }
            } else {
                setContent {
                    VocaTheme {
                        DebugScreen("Permission Required! Please check app settings.")
                    }
                }
            }
        }
        permissionLauncher.launch(requiredPermissions)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

@Composable
fun CameraScreen(topicName: String, keywords: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    var recording: Recording? by remember { mutableStateOf(null) }
    var isRecording by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var timerText by remember { mutableStateOf("00:00") }
    var seconds by remember { mutableStateOf(0) }
    var cacheFile by remember { mutableStateOf<File?>(null) }


    fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun startCamera() {
        if (!hasPermissions()) return

        cameraProviderFuture.addListener({
            val provider = cameraProviderFuture.get()
            cameraProvider = provider
            provider.unbindAll()

            val preview = Preview.Builder()
                .build()
                .apply {
                    setSurfaceProvider(previewView?.surfaceProvider)
                }

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.SD))
                .setExecutor(ContextCompat.getMainExecutor(context))
                .build()

            videoCapture = VideoCapture.withOutput(recorder)

            try {
                provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    videoCapture
                )
            } catch (exc: Exception) {
                Log.e("CameraScreen", "Camera binding failed: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    LaunchedEffect(isRecording, isPaused) {
        while (isRecording && !isPaused) {
            timerText = String.format("%02d:%02d", seconds / 60, seconds % 60)
            kotlinx.coroutines.delay(1000L)
            seconds++
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).also {
                    previewView = it
                    startCamera()
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // UI Overlay
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section with Timer and Topic
            TopSection(timerText, topicName)

            // Bottom Section with Controls
            BottomSection(
                isRecording = isRecording,
                isPaused = isPaused,
                keywords = keywords,
                onSwitchCamera = {
                    cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    else
                        CameraSelector.DEFAULT_BACK_CAMERA
                    startCamera()
                },
                onRecordingClick = {
                    if (isRecording) {
                        recording?.stop()
                        recording = null
                        isRecording = false
                        isPaused = false
                        seconds = 0
                        timerText = "00:00"

                        // Ensure cacheFile is not null before proceeding
                        cacheFile?.let {
                            val intent = Intent(context, PreviewActivity::class.java).apply {
                                putExtra("VIDEO_PATH", it.absolutePath) // Pass video path
                                putExtra("TOPIC_NAME", topicName) // Pass topic name
                            }
                            context.startActivity(intent)
                        }
                    } else {
                        if (!hasPermissions()) return@BottomSection

                        // Define the cache file location
                        cacheFile = File(
                            context.cacheDir,
                            "VID_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.mp4"
                        )

                        val fileOutputOptions = FileOutputOptions.Builder(cacheFile!!).build()

                        recording = videoCapture?.output
                            ?.prepareRecording(context, fileOutputOptions)
                            ?.apply { withAudioEnabled() }
                            ?.start(ContextCompat.getMainExecutor(context)) {}

                        isRecording = true
                    }
                }


                ,
                onPauseResumeClick = {
                    if (isRecording) {
                        if (!isPaused) {
                            recording?.pause()
                        } else {
                            recording?.resume()
                        }
                        isPaused = !isPaused
                    }
                }
            )
        }
    }
}

@Composable
fun TopSection(timerText: String, topicName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(16.dp)
    ) {
        // Timer Display
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = timerText,
                color = Color.White,
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Topic Name
        Text(
            text = topicName,
            color = Color.White,
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun BottomSection(
    isRecording: Boolean,
    isPaused: Boolean,
    keywords: String,
    onSwitchCamera: () -> Unit,
    onRecordingClick: () -> Unit,
    onPauseResumeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.6f))
    ) {
        // Keywords Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            backgroundColor = Color.White.copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp),
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Keywords",
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    keywords,
                    color = Color.White,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        // Camera Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Switch Camera Button
            IconButton(
                onClick = onSwitchCamera,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Icon(
                    Icons.Filled.SwitchCamera,
                    contentDescription = "Switch Camera",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Record Button
            IconButton(
                onClick = onRecordingClick,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        if (isRecording) Color.Red
                        else Color.Red.copy(alpha = 0.8f)
                    )
            ) {
                Icon(
                    if (isRecording) Icons.Filled.Stop
                    else Icons.Filled.Videocam,
                    contentDescription = if (isRecording) "Stop Recording"
                    else "Start Recording",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Pause/Resume Button
            IconButton(
                onClick = onPauseResumeClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Icon(
                    if (isPaused) Icons.Filled.PlayArrow
                    else Icons.Filled.Pause,
                    contentDescription = if (isPaused) "Resume Recording"
                    else "Pause Recording",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DebugScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.error
        )
    }
}