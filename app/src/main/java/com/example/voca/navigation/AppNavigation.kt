package com.example.voca.navigation

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.voca.ui.theme.screen.RegisterScreen
import com.example.voca.activities.ActivityListActivity
import com.example.voca.activities.CameraActivity
import com.example.voca.viewmodel.RegisterViewModel
import com.example.voca.ui.theme.screen.HomeScreen
import com.example.voca.ui.theme.screen.LessonDetailScreen
import com.example.voca.ui.theme.screen.PathActivity
import com.example.voca.ui.theme.screen.ProfileScreen
import com.example.voca.ui.theme.screen.WordOfTheDayScreen
import com.example.voca.ui.theme.screen.ReportScreen
import com.example.voca.ui.theme.screen.SignInScreen
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(viewModel: RegisterViewModel) {
    val navController = rememberNavController()
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "home" else "register"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("register") {
            RegisterScreen(viewModel = viewModel, navController = navController)
        }
        composable("signin") {
            SignInScreen(viewModel = viewModel, navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen()
        }
        composable("word_of_the_day") {
            WordOfTheDayScreen()
        }
        composable("result") {
            ReportScreen()
        }
        // For "path_activity", we now call the function defined in HomeScreen.
        // Ensure that HomeScreen.kt has the PathActivity(navController) function that does the redirection.
        composable("path_activity") {
            PathActivity(navController)
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

        composable(
            route = "lessonDetail/{lessonId}",
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId")
            if (lessonId != null) {
                LessonDetailScreen(lessonId = lessonId, navController = navController)
            }
        }
    }
}
