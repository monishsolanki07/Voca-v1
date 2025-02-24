package com.example.voca.navigation

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.voca.ui.theme.screen.RegisterScreen
import com.example.voca.activities.ActivityListActivity
import com.example.voca.activities.CameraActivity
import com.example.voca.viewmodel.RegisterViewModel
import com.example.voca.ui.theme.screen.HomeScreen
import com.example.voca.ui.theme.screen.PathActivity
import com.example.voca.ui.theme.screen.ProfileScreen
import com.example.voca.ui.theme.screen.WordOfTheDayScreen
import com.example.voca.ui.theme.screen.SignInScreen
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(viewModel: RegisterViewModel) {
    val navController = rememberNavController()

    // Check if a Firebase user exists (i.e., account created)
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "home" else "register"

    NavHost(navController = navController, startDestination = startDestination) {
        // Register Screen
        composable("register") {
            RegisterScreen(viewModel = viewModel, navController = navController)
        }
        // Signin screen
        composable("signin") {
            SignInScreen(viewModel = viewModel, navController = navController)
        }

        // Home Screen
        composable("home") {
            HomeScreen(navController = navController)
        }
        // Profile Screen
        composable("profile") {
            ProfileScreen()
        }
        // Word Of The Day Screen
        composable("word_of_the_day") {
            WordOfTheDayScreen()
        }
        composable("path_activity") {
            PathActivity()
        }
        composable("camera_activity") {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                val intent = Intent(context, CameraActivity::class.java)
                context.startActivity(intent)
            }
        }
        composable("activity_list") {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                val intent = Intent(context, ActivityListActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}
