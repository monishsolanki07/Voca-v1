package com.example.voca.ui.theme.screen

import android.os.Build
import android.util.Log
import android.widget.Toast
import android.content.Context
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.voca.network.RegisterRequest
import com.example.voca.network.User
import com.example.voca.utils.encodePublicKeyToBase64
import com.example.voca.utils.getPublicKey
import com.example.voca.utils.generateKeyPair
import com.example.voca.viewmodel.RegisterViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var publicKey by remember { mutableStateOf("") }

    val alias = "myAppKeyAlias"
    val context = LocalContext.current

    // Admin credentials for demonstration
    val adminUsername = "admin"
    val adminPassword = "admin07"

    // Generate key pair and exchange public key immediately.
    LaunchedEffect(Unit) {
        Log.d("RegisterScreen", "Generating key pair...")
        generateKeyPair(alias)
        val key = getPublicKey(alias)
        if (key != null) {
            publicKey = encodePublicKeyToBase64(key)
            Log.d("RegisterScreen", "Key pair generated. Public key: $publicKey")
            viewModel.exchangePublicKey(publicKey,
                onSuccess = {
                    Log.d("RegisterScreen", "Public key exchange successful.")
                },
                onError = { error ->
//                    Toast.makeText(context, "Public key exchange failed: $error", Toast.LENGTH_SHORT).show()
                })
        } else {
            Log.e("RegisterScreen", "Failed to generate key pair.")
        }
    }

    // UI layout remains the same—nothing removed, especially your server side call.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Admin bypass for demonstration purposes.
                    if (username == adminUsername && password == adminPassword) {
                        // Store username in SharedPreferences
                        val sharedPref = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("username", username)
                            apply()
                        }
                        navController.navigate("home")
                        return@Button
                    }

                    if (username.isNotEmpty() && firstName.isNotEmpty() &&
                        lastName.isNotEmpty() && email.isNotEmpty() &&
                        password.isNotEmpty()
                    ) {
                        val user = User(
                            username = username,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password
                        )
                        // This RegisterRequest, including the public key, is your server side registration data.
                        val request = RegisterRequest(data = listOf(user), publicKey = publicKey)

                        // Calling your ViewModel method that handles Firebase auth,
                        // and then invokes the server side registration without changing your backend.
                        viewModel.registerUserWithFirebase(
                            email = email,
                            password = password,
                            request = request,
                            onSuccess = {
                                // Store username in SharedPreferences
                                val sharedPref = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putString("username", username)
                                    apply()
                                }
                                navController.navigate("home")
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // New button to navigate to the Sign In screen.
            TextButton(
                onClick = { navController.navigate("signin") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Already have an account? Sign In")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Skip Registration")
            }
        }
    }
}
