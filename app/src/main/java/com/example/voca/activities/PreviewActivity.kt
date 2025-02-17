package com.example.voca.activities



import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import android.content.ContentValues
import android.provider.MediaStore
import java.io.File

import java.io.OutputStream


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

    // Initialize ExoPlayer
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
            // Video Player (3/4th of the screen)
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = true
                        this.player = player
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f) // Takes 75% of the screen height
            )

            Spacer(modifier = Modifier.height(16.dp)) // Space below the player

            // Download Button
            Button(
                onClick = { downloadVideo(context, videoPath) },
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Half of the screen width
                    .padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Download, contentDescription = "Download Video")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Download")
            }
        }
    }
}

// Function to handle video download
private fun downloadVideo(context: Context, videoPath: String) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Video.Media.DISPLAY_NAME, File(videoPath).name)
        put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Voca") // Saves to Movies/Voca folder
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
