package com.example.voca.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.voca.ui.RegisterScreen
import com.example.voca.ui.theme.CameraActivity
import com.example.voca.viewmodel.RegisterViewModel
import com.example.voca.ui.theme.HomeScreen
import com.example.voca.ui.theme.PathActivity
import com.example.voca.ui.theme.ProfileScreen
import com.example.voca.ui.theme.WordOfTheDayScreen // Import WordOfTheDayScreen

@Composable
fun AppNavigation(viewModel: RegisterViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "register") {
        // Register Screen
        composable("register") {
            RegisterScreen(viewModel = viewModel, navController = navController)
        }
        // Home Screen
        composable("home") {
            HomeScreen(navController = navController)
        }
        // Profile Screen
        composable("profile") {
            ProfileScreen() // Navigate to Profile Screen
        }
        // Word Of The Day Screen
        composable("word_of_the_day") {
            WordOfTheDayScreen() // Navigate to Word Of The Day
        }
        composable("path_activity") {
            PathActivity() // Navigate to Word Of The Day
        }
        composable("camera_activity") {
            val context = LocalContext.current // ✅ Get context inside the composable

            // 🔥 Start CameraActivity using Intent
            LaunchedEffect(Unit) {
                val intent = Intent(context, CameraActivity::class.java)
                context.startActivity(intent)
            }
        }

    }
}
