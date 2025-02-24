package com.example.voca.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voca.network.ApiService
import com.example.voca.network.PublicKeyExchangeRequest
import com.example.voca.network.PublicKeyExchangeResponse
import com.example.voca.network.RegisterRequest
import com.example.voca.network.RegisterResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume

class RegisterViewModel : ViewModel() {
    private val apiService: ApiService
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    // Exchange public key (server-side, untouched because your backend is perfect)
    fun exchangePublicKey(publicKey: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        Log.d("RegisterViewModel", "Sending public key to the server: $publicKey")
        val request = PublicKeyExchangeRequest(publicKey)
        apiService.exchangePublicKey(request).enqueue(object : Callback<PublicKeyExchangeResponse> {
            override fun onResponse(call: Call<PublicKeyExchangeResponse>, response: Response<PublicKeyExchangeResponse>) {
                if (response.isSuccessful) {
                    Log.d("RegisterViewModel", "Public key exchange successful: ${response.body()?.status}")
                    onSuccess()
                } else {
                    Log.e("RegisterViewModel", "Public key exchange failed: ${response.message()}")
                    onError("Public key exchange failed: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<PublicKeyExchangeResponse>, t: Throwable) {
                Log.e("RegisterViewModel", "Public key exchange failed: ${t.message}")
                onError("Public key exchange failed: ${t.message}")
            }
        })
    }

    // Updated: Register user with Firebase (firewall) authentication using coroutines
    fun registerUserWithFirebase(
        email: String,
        password: String,
        request: RegisterRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RegisterViewModel", "Firebase registration successful")
                    // Immediately navigate since Firebase accepted the user.
                    onSuccess()
                    // Now perform the server-side registration separately.
                    registerUser(request,
                        onSuccess = {
                            Log.d("RegisterViewModel", "Server-side registration completed (asynchronous)")
                        },
                        onError = { error ->
                            Log.e("RegisterViewModel", "Server-side registration error: $error")
                        }
                    )
                } else {
                    Log.e("RegisterViewModel", "Firebase registration failed: ${task.exception?.message}")
                    onError("Firebase registration failed: ${task.exception?.message}")
                }
            }
    }


    // Suspend function for Firebase registration using suspendCancellableCoroutine
    private suspend fun registerWithFirebase(email: String, password: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                    }
                }
        }

    // Register user with server-side API call (keeping your backend code untouched)
    fun registerUser(request: RegisterRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        apiService.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Registration failed: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                onError("Registration failed: ${t.message}")
            }
        })
    }

    fun signInUserWithFirebase(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RegisterViewModel", "Firebase sign-in successful")
                    onSuccess()
                } else {
                    Log.e("RegisterViewModel", "Firebase sign-in failed: ${task.exception?.message}")
                    onError("Firebase sign-in failed: ${task.exception?.message}")
                }
            }
    }

}
