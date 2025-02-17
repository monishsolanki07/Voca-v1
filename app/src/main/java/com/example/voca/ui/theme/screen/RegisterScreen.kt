package com.example.voca.ui.theme.screen


import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
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

    // Generate key pair and get public key
    LaunchedEffect(Unit) {
        Log.d("RegisterViewModel", "Generating key pair...")
        generateKeyPair(alias)  // Generating the key pair
        val key = getPublicKey(alias)
        if (key != null) {
            publicKey = encodePublicKeyToBase64(key)
            Log.d("RegisterViewModel", "Key pair generated. Public key: $publicKey")

            // Immediately exchange the public key after generation
            viewModel.exchangePublicKey(publicKey,
                onSuccess = {
                    Log.d("RegisterViewModel", "Public key exchange successful.")
                },
                onError = { error ->
                    Toast.makeText(context, "Public key exchange failed: $error", Toast.LENGTH_SHORT).show()
                })
        } else {
            Log.e("RegisterViewModel", "Failed to generate key pair.")
        }
    }

    // UI components (TextFields, Buttons, etc.)
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

            // Username TextField
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // First Name TextField
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Last Name TextField
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            Button(
                onClick = {
                    // Admin authentication bypass
                    if (username == adminUsername && password == adminPassword) {
                        navController.navigate("home")
                        return@Button
                    }

                    // Regular registration validation
                    if (username.isNotEmpty() && firstName.isNotEmpty() &&
                        lastName.isNotEmpty() && email.isNotEmpty() &&
                        password.isNotEmpty()) {
                        val user = User(
                            username = username,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password
                        )

                        val request = RegisterRequest(data = listOf(user), publicKey = publicKey)

                        viewModel.registerUser(request,
                            onSuccess = { navController.navigate("home") },
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

            // Skip Button
            TextButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Skip Registration")
            }
        }
    }
}
