package com.example.voca

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.voca.navigation.AppNavigation
import com.example.voca.ui.theme.screen.RegisterScreen
import com.example.voca.viewmodel.RegisterViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = RegisterViewModel() // Initialize ViewModel
        setContent {
            AppNavigation(viewModel = viewModel) // Load RegisterScreen
        }
    }
}
