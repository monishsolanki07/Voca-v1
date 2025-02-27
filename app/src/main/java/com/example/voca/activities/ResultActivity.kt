package com.example.voca.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the download URL (stored internally, not shown to user)
        val downloadUrl = intent.getStringExtra("DOWNLOAD_URL")
        setContent {
            // Create a local NavController for navigation within this activity
            val navController = rememberNavController()
            ResultScreen(downloadUrl = downloadUrl, navController = navController)
        }
    }
}

@Composable
fun ResultScreen(downloadUrl: String?, navController: NavController) {
    // Although the download URL is retrieved, it's kept internally (you might log it).
    // The UI displays a popup card with a success message and a button to return home.
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = 8.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Video uploaded successfully!",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Navigate to the "home" route defined in your navigation graph
                        navController.navigate("home")
                    }
                ) {
                    Text(text = "Return to Home")
                }
            }
        }
    }
}
