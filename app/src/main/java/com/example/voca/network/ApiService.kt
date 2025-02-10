package com.example.voca.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Request data structure for registering a user
data class RegisterRequest(
    val data: List<User>,
    val publicKey: String
)

// Request data structure for exchanging the public key
data class PublicKeyExchangeRequest(
    val publicKey: String
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

// Response data structure for registration
data class RegisterResponse(
    val uniqueKey: String?
)

// Response data structure for public key exchange
data class PublicKeyExchangeResponse(
    val status: String,
    val message: String
)

// Retrofit interface for the API
interface ApiService {
    @POST("register")  // No trailing slash here!
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("exchange-public-key") // Public key exchange endpoint
    fun exchangePublicKey(@Body request: PublicKeyExchangeRequest): Call<PublicKeyExchangeResponse>
}
