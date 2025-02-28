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
    val timestamp: String,  // Must be in ISO 8601 format
    val key: String,        // Base64 Encoded Public Key
    val userID: String      // Username
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
    val message: String,
    val pubkey: String
)


// New data classes for video upload via firewall
data class VideoUploadRequest(
    val fileName: String,
    val videoUrl: String
)

data class VideoUploadResponse(
    val downloadUrl: String
)

// Retrofit interface for the API
interface ApiService {
    @POST("register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("api/key")
    fun exchangePublicKey(@Body request: PublicKeyExchangeRequest): Call<PublicKeyExchangeResponse>


    // New endpoint for sending the video URL to the firewall
    @POST("uploadVideo")
    fun uploadVideo(@Body request: VideoUploadRequest): Call<VideoUploadResponse>
}
