package com.example.voca.ui.theme.screen

import android.content.Intent
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.voca.R

import com.example.voca.ui.theme.screen.ProfileScreen


// Define a consistent color palette
private val primaryColor = Color(0xFF9C7FE4)
private val primaryVariant = Color(0xFF7E57C2)
private val secondaryColor = Color(0xFFD1A4E8)
private val accentColor = Color(0xFF6A1B9A)
private val lightPurple = Color(0xFFD1C4E9)
private val veryLightPurple = Color(0xFFF3E5F5)
private val textPrimary = Color(0xFF333333)
private val textSecondary = Color(0xFF666666)

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
    val bottomNavHeight = 60.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Voca",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                backgroundColor = primaryColor,
                contentColor = Color.White,
                elevation = 8.dp,
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.White,
                contentColor = primaryColor,
                elevation = 16.dp,
                modifier = Modifier.height(bottomNavHeight)
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
                            }
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(if (selectedItem == index) 40.dp else 30.dp)
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = icons[index],
                                    contentDescription = item,
                                    modifier = Modifier.size(if (selectedItem == index) 28.dp else 24.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.caption.copy(
                                    fontWeight = if (selectedItem == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            )
                        },
                        selectedContentColor = primaryColor,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(veryLightPurple.copy(alpha = 0.5f))
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = bottomNavHeight)
            ) {
                // Hero Image Section (Improved)
                HeroImageSection()

                // Content with padding
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    // Word of the Day Banner
                    WordOfTheDayBanner(navController)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Speech Tips Section
                    SpeechTipsSection()

                    // Path Activity Section
                    PathActivityCard(navController)

                    // Result View Card Section
                    ResultViewCard(navController)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contact Us Section
                    ContactUsSection()

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun HeroImageSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        // Background Image with ContentScale.Crop to ensure full visibility
        Image(
            painter = painterResource(id = R.drawable.a1),
            contentDescription = "Feature image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay for better text visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        ),
                        startY = 300f,
                        endY = 900f
                    )
                )
        )

        // Text overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Text(
                    text = "Enhance Your Vocabulary",
                    style = MaterialTheme.typography.h5.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Learn, practice, and master new words daily",
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

@Composable
fun WordOfTheDayBanner(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(top = 16.dp)
            .clickable { navController.navigate("word_of_the_day") },
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(primaryColor, secondaryColor)
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoStories,
                        contentDescription = "Word of the Day Icon",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = "Discover the Word of the Day!",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap to reveal the word and its meaning.",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ResultViewCard(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(top = 16.dp)
            .clickable { navController.navigate("result") },
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(primaryColor, secondaryColor)
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoStories,
                        contentDescription = "Result",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = "Check your Result here!",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Know how you performed in the task",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SpeechTipsSection() {
    Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
        Text(
            "Speech Tips",
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold,
                color = accentColor
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SpeechTipCard(
            icon = Icons.Filled.Mic,
            title = "Speak Clearly",
            description = "Ensure your words are articulated properly and not rushed."
        )

        SpeechTipCard(
            icon = Icons.Filled.Accessibility,
            title = "Practice Breathing",
            description = "Control your speech with regular and proper breathing for better delivery."
        )

        SpeechTipCard(
            icon = Icons.Filled.Pause,
            title = "Use Pauses Effectively",
            description = "Pauses can help convey meaning and allow the listener to absorb the message."
        )
    }
}

@Composable
fun SpeechTipCard(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = primaryColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    description,
                    style = MaterialTheme.typography.body2.copy(
                        color = textSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun PathActivityCard(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(vertical = 12.dp)
            .clickable { navController.navigate("path_activity") },
        elevation = 6.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(primaryVariant, primaryColor)
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.FitnessCenter,
                        contentDescription = "Activity",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = "Discover the World of Learning!",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap to reveal the Path",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ResultViewCard() {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(vertical = 12.dp)
            ,
        elevation = 6.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(accentColor, primaryVariant)
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Assessment,
                        contentDescription = "Result View",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = "View Results",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap here to view detailed results.",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ContactUsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 6.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Contact Us",
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

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

@Composable
fun ContactRow(icon: ImageVector, title: String, details: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(veryLightPurple),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = primaryColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            )
            Text(
                text = details,
                style = MaterialTheme.typography.body2.copy(
                    color = textSecondary
                )
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
        composable("profile") { ProfileScreen() }
        // Other routes can be added as needed
    }
}