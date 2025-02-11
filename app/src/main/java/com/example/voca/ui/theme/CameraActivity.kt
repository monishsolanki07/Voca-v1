package com.example.voca.ui.theme

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SwitchCamera
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        requestPermissions()
    }

    private fun requestPermissions() {
        val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true && permissions[Manifest.permission.RECORD_AUDIO] == true) {
                setContent { CameraScreen() }
            } else {
                setContent { DebugScreen("Permissions Denied! Check App Settings.") }
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
fun CameraScreen() {
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

    fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    fun startCamera() {
        if (!hasPermissions()) return
        cameraProviderFuture.addListener({
            val provider = cameraProviderFuture.get()
            cameraProvider = provider
            provider.unbindAll()
            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(previewView?.surfaceProvider)
            }
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .setExecutor(ContextCompat.getMainExecutor(context))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)
            try {
                provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, videoCapture)
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
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).also { previewView = it; startCamera() }
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxWidth().background(Color.Black).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(timerText, color = Color.White, style = MaterialTheme.typography.h4)
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxWidth().background(Color.DarkGray).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Topic Heading", color = Color.White, style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Keyword Section: Example keywords here", color = Color.White, style = MaterialTheme.typography.body1)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().background(Color.Black).padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                    startCamera()
                }) {
                    Icon(Icons.Filled.SwitchCamera, contentDescription = "Switch Camera", tint = Color.White)
                }
                IconButton(
                    modifier = Modifier.size(72.dp),
                    onClick = {
                        if (isRecording) {
                            recording?.stop()
                            recording = null
                            isRecording = false
                            isPaused = false
                            seconds = 0
                            timerText = "00:00"
                        } else {
                            if (!hasPermissions()) return@IconButton
                            val contentValues = ContentValues().apply {
                                put(MediaStore.Video.Media.DISPLAY_NAME, "VID_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.mp4")
                            }
                            val mediaStoreOutput = MediaStoreOutputOptions.Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                                .setContentValues(contentValues)
                                .build()
                            recording = videoCapture?.output?.prepareRecording(context, mediaStoreOutput)?.apply { withAudioEnabled() }?.start(ContextCompat.getMainExecutor(context)) {}
                            isRecording = true
                        }
                    }
                ) {
                    Icon(if (isRecording) Icons.Filled.Stop else Icons.Filled.Videocam, contentDescription = "Start/Stop Recording", tint = Color.White)
                }
                IconButton(onClick = {
                    if (isRecording) {
                        if (!isPaused) {
                            recording?.pause()
                        } else {
                            recording?.resume()
                        }
                        isPaused = !isPaused
                    }
                }) {
                    Icon(if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause, contentDescription = "Pause/Resume Recording", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun DebugScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, style = MaterialTheme.typography.h5)
    }
}
