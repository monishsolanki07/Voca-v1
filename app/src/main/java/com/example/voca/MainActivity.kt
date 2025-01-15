package com.example.voca



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.voca.ui.theme.RegisterScreen
import com.example.voca.viewmodel.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = RegisterViewModel() // Initialize ViewModel
        setContent {
            RegisterScreen(viewModel) // Load RegisterScreen
        }
    }
}
