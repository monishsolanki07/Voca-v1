package com.example.voca.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//val Purple40 = Color(0xFF6650a4)
//val Purple80 = Color(0xFFD0BCFF)
//val PurpleGrey40 = Color(0xFF625b71)
val PurpleLighter = Color(0xFFF0E6FF)

@Composable
fun HomeScreen() {
    // Animation states
    val cardScale = remember { Animatable(0.8f) }
    LaunchedEffect(Unit) {
        cardScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                backgroundColor = Purple40,
                contentColor = Color.White,
                elevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Voca",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Card(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        border = BorderStroke(2.dp, Purple80),
                        backgroundColor = PurpleGrey40
                    ) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = Purple40,
                elevation = 8.dp
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    selected = true,
                    onClick = { /* Handle Home click */ },
                    label = { Text("Home") },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(alpha = 0.7f)
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    selected = false,
                    onClick = { /* Handle Search click */ },
                    label = { Text("Search") },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(alpha = 0.7f)
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notifications") },
                    selected = false,
                    onClick = { /* Handle Notifications click */ },
                    label = { Text("Notifications") },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(alpha = 0.7f)
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    selected = false,
                    onClick = { /* Handle Profile click */ },
                    label = { Text("Profile") },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome Card with Animation
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .scale(cardScale.value),
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, Purple40)
            ) {
                Box(
                    modifier = Modifier
                        .background(PurpleLighter)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome to Voca!",
                            style = MaterialTheme.typography.h4.copy(
                                color = Purple40,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your personal vocabulary assistant",
                            style = MaterialTheme.typography.subtitle1,
                            color = PurpleGrey40
                        )
                    }
                }
            }

            // Feature Cards
            FeatureCard(
                icon = Icons.Filled.Book,
                title = "Daily Vocabulary",
                description = "Learn 5 new words every day"
            )

            FeatureCard(
                icon = Icons.Filled.Quiz,
                title = "Practice Quiz",
                description = "Test your knowledge with interactive quizzes"
            )

            FeatureCard(
                icon = Icons.Filled.Bookmark,
                title = "Saved Words",
                description = "Review your saved vocabulary"
            )

            FeatureCard(
                icon = Icons.Filled.TrendingUp,
                title = "Progress Tracker",
                description = "Monitor your learning journey"
            )
        }
    }
}

@Composable
fun FeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { /* Handle click */ },
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Purple40,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6.copy(
                        color = Purple40,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.body2,
                    color = PurpleGrey40
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}