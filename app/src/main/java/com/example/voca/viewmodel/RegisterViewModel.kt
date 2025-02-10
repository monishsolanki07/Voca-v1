package com.example.voca.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.voca.network.ApiService
import com.example.voca.network.PublicKeyExchangeRequest
import com.example.voca.network.PublicKeyExchangeResponse
import com.example.voca.network.RegisterRequest
import com.example.voca.network.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class RegisterViewModel : ViewModel() {
    private val apiService: ApiService

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

    // Public key exchange at app startup
    fun exchangePublicKey(publicKey: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // Log the public key before sending it to the server
        Log.d("RegisterViewModel", "Sending public key to the server: $publicKey")

        val request = PublicKeyExchangeRequest(publicKey)  // Create request with the public key

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


    // Register user with public key
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
}
