package com.example.voca.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.vector.ImageVector


import com.example.voca.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Profile", "Activity", "Quiz")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Person,
        Icons.Filled.FitnessCenter,
        Icons.Filled.QuestionAnswer
    )
    val bottomNavHeight = 56.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voca", fontWeight = FontWeight.Bold) },
                backgroundColor = Color(0xFF9C7FE4),
                contentColor = Color.White,
                elevation = 8.dp,
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color(0xFF9C7FE4),
                contentColor = Color.White
            ) {
                items.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            when (index) {
                                1 -> navController.navigate("profile")
                                2 -> navController.navigate("camera_activity")
                                3 -> navController.navigate("activity_list")
                                // Add other navigation logic if required
                            }
                        },
                        icon = { Icon(imageVector = icons[index], contentDescription = item) },
                        label = {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.body2.copy(color = Color.White)
                            )
                        },
                        selectedContentColor = Color(0xFFD1A4E8),
                        unselectedContentColor = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = bottomNavHeight)
            ) {
                // Image Section
                DisplayImageSection()

                // Word of the Day Banner
                WordOfTheDayBanner(navController)

                Spacer(modifier = Modifier.height(16.dp))

                // Speech Tips Section
                SpeechTipsSection()

                // Path Activity Section
                PathActivity(navController)

                Spacer(modifier = Modifier.height(24.dp))

                // Contact Us Section
                ContactUsSection()
            }
        }
    }
}

/**
 * Displays an image with a border and padding.
 */
@Composable
fun DisplayImageSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.a1),
            contentDescription = "Image placeholder",
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Displays the Word of the Day banner, which is clickable and navigates to a different screen.
 */
@Composable
fun WordOfTheDayBanner(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 20.dp)
            .clickable { navController.navigate("word_of_the_day") },
        elevation = 10.dp,
        shape = RoundedCornerShape(24.dp),
        backgroundColor = Color(0xFFD1C4E9)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AutoStories,
                contentDescription = "Word of the Day Icon",
                tint = Color(0xFF7E57C2),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = "Discover the Word of the Day!",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6A1B9A)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap to reveal the word and its meaning.",
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

/**
 * Displays speech tips in a list format with cards for each tip.
 */
@Composable
fun SpeechTipsSection() {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            "Speech Tips",
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SpeechTipCard(
            icon = Icons.Filled.Mic,
            title = "1. Speak Clearly",
            description = "Ensure your words are articulated properly and not rushed."
        )

        SpeechTipCard(
            icon = Icons.Filled.Accessibility,
            title = "2. Practice Breathing",
            description = "Control your speech with regular and proper breathing for better delivery."
        )

        SpeechTipCard(
            icon = Icons.Filled.Pause,
            title = "3. Use Pauses Effectively",
            description = "Pauses can help convey meaning and allow the listener to absorb the message."
        )
    }
}

/**
 * Displays an individual speech tip in a card format.
 */
@Composable
fun SpeechTipCard(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        elevation = 4.dp,
        backgroundColor = Color(0xFFEDE7F6)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF9C7FE4),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    description,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

/**
 * Displays the Path Activity banner, which is clickable and navigates to a different screen.
 */
@Composable
fun PathActivity(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(vertical = 20.dp)
            .clickable { navController.navigate("path_activity") },
        elevation = 10.dp,
        shape = RoundedCornerShape(24.dp),
        backgroundColor = Color(0xFFD1C4E9)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.FitnessCenter,
                contentDescription = "Activity",
                tint = Color(0xFF7E57C2),
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = "Discover the World of Learning!",
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6A1B9A)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap to reveal the Path",
                    style = MaterialTheme.typography.body1.copy(
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

/**
 * Displays the Contact Us section with email, phone, website, and social media details.
 */
@Composable
fun ContactUsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color(0xFFF3E5F5)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Contact Us",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A1B9A)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Add multiple contact options
            ContactRow(
                icon = Icons.Filled.Email,
                title = "Email Us",
                details = "contact@vocaapp.com"
            )
            ContactRow(
                icon = Icons.Filled.Phone,
                title = "Call Us",
                details = "+1 234 567 890"
            )
            ContactRow(
                icon = Icons.Filled.Language,
                title = "Visit Our Website",
                details = "www.vocaapp.com"
            )
            ContactRow(
                icon = Icons.Filled.Share,
                title = "Follow Us",
                details = "@VocaApp on all platforms"
            )
        }
    }
}

/**
 * Displays a single contact row with an icon, title, and details.
 */
@Composable
fun ContactRow(icon: ImageVector, title: String, details: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF8E24AA),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A1B9A)
                )
            )
            Text(
                text = details,
                style = MaterialTheme.typography.body2.copy(color = Color.DarkGray)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController) }
        composable("profile") { ProfileScreen() } // ProfileScreen from ProfileScreen.kt
    }
}
