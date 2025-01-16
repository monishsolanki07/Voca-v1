package com.example.voca.navigation

import RegisterScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.voca.ui.theme.HomeScreen
import com.example.voca.viewmodel.RegisterViewModel

@Composable
fun AppNavigation(viewModel: RegisterViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "register") {
        composable("register") { RegisterScreen(viewModel, navController) }
        composable("home") { HomeScreen() }
    }
}
