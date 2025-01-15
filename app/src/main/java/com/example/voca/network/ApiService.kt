package com.example.voca.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Request data structure
// Updated Request structure with 'data' key containing a list of users
data class RegisterRequest(
    val data: List<User>
)


// User details structure
data class User(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val gender: String = "male",
    val country: String = "india"
)

// Response data structure
data class RegisterResponse(
    val uniqueKey: String?
)

// Retrofit interface for the API
interface ApiService {
    @POST("register")  // No trailing slash here!
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>
}
