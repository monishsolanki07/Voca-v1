package com.example.voca.viewmodel

import androidx.lifecycle.ViewModel
import com.example.voca.network.ApiService
import com.example.voca.network.RegisterRequest
import com.example.voca.network.RegisterResponse
import com.example.voca.network.User
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
        // Create a logging interceptor
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY  // Log request/response body

        // Create OkHttpClient with the logging interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Pass OkHttpClient with logging to Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/")  // Use 10.0.2.2 to access the host machine from the emulator
            .client(client)  // Attach the custom OkHttpClient to Retrofit
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    // Update method to accept RegisterRequest instead of User
    fun registerUser(request: RegisterRequest, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        apiService.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    response.body()?.uniqueKey?.let {
                        onSuccess("Registration successful: $it")
                    } ?: onError("Empty response body")
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
