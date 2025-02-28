package com.example.voca.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.voca.network.ApiService
import com.example.voca.network.PublicKeyExchangeRequest
import com.example.voca.network.PublicKeyExchangeResponse
import com.example.voca.network.RegisterRequest
import com.example.voca.network.RegisterResponse
import com.example.voca.network.VideoUploadRequest
import com.example.voca.network.VideoUploadResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import com.example.voca.utils.getCurrentTimestamp

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val apiService: ApiService

    init {
        // Retrofit is used for server API calls (e.g., public key exchange, user registration, firewall upload)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Update the base URL with your server's public endpoint if needed.
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/") // Server-side endpoint
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    // --- Server-Side Functions (For Public Key Exchange, Registration, etc.) ---

    // Exchange public key with the server.
    fun exchangePublicKey(publicKey: String, userID: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val timestamp = getCurrentTimestamp()
        Log.d("RegisterViewModel", "Sending public key to the server: $publicKey")
        val request = PublicKeyExchangeRequest(timestamp, publicKey, userID)
        apiService.exchangePublicKey(request).enqueue(object : Callback<PublicKeyExchangeResponse> {
            override fun onResponse(call: Call<PublicKeyExchangeResponse>, response: Response<PublicKeyExchangeResponse>) {
                if (response.isSuccessful) {
                    Log.d("RegisterViewModel", "Public key exchange successful: ${response.body()?.pubkey}")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: response.message()
                    Log.e("RegisterViewModel", "Public key exchange failed: $errorBody")
                    onError("Public key exchange failed: $errorBody")
                }

            }
            override fun onFailure(call: Call<PublicKeyExchangeResponse>, t: Throwable) {
                Log.e("RegisterViewModel", "Public key exchange failed: ${t.message}")
                onError("Public key exchange failed: ${t.message}")
            }
        })
    }

    // Register user with Firebase authentication.
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

    // Suspend function for Firebase registration using coroutines.
    private suspend fun registerWithFirebase(email: String, password: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    continuation.resume(task.isSuccessful)
                }
        }

    // Register user with a server-side API call.
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

    // Sign in user with Firebase.
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

    // --- Independent Firebase Function for Video Upload ---

    /**
     * Uploads a video directly to Firebase Storage.
     * The video is stored under the "videos/" folder with a filename formatted as "topicName_videoId.mp4".
     * On success, returns the Firebase download URL.
     */
    fun uploadVideoFirebase(
        topicName: String,
        videoUri: Uri,
        onUploadSuccess: (String) -> Unit,
        onUploadFailure: (String) -> Unit
    ) {
        val videoId = System.currentTimeMillis().toString()
        val formattedName = "${topicName}_${videoId}.mp4"  // e.g., MyTopic_1634567890123.mp4
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference.child("videos/$formattedName")

        storageRef.putFile(videoUri)
            .addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("RegisterViewModel", "Firebase video uploaded successfully: $uri")
                    onUploadSuccess(uri.toString())
                }.addOnFailureListener { exception ->
                    Log.e("RegisterViewModel", "Failed to retrieve Firebase download URL: ${exception.message}")
                    onUploadFailure("Failed to retrieve Firebase download URL: ${exception.message}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("RegisterViewModel", "Firebase video upload failed: ${exception.message}")
                onUploadFailure("Firebase video upload failed: ${exception.message}")
            }
    }

    // --- Server-Side Function for Video Upload (Firewall) ---

    /**
     * Sends the uploaded video's Firebase URL and formatted filename to the server.
     * The server can then process/store the video independently.
     */
    fun sendVideoToFirewall(
        topicName: String,
        firebaseDownloadUrl: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val videoId = System.currentTimeMillis().toString()
        val formattedName = "${topicName}_${videoId}.mp4"
        val request = VideoUploadRequest(formattedName, firebaseDownloadUrl)

        apiService.uploadVideo(request).enqueue(object : Callback<VideoUploadResponse> {
            override fun onResponse(call: Call<VideoUploadResponse>, response: Response<VideoUploadResponse>) {
                if (response.isSuccessful) {
                    val downloadUrl = response.body()?.downloadUrl ?: ""
                    Log.d("RegisterViewModel", "Server returned download URL: $downloadUrl")
                    onSuccess(downloadUrl)
                } else {
                    val error = "Server video upload failed: ${response.message()}"
                    Log.e("RegisterViewModel", error)
                    onError(error)
                }
            }
            override fun onFailure(call: Call<VideoUploadResponse>, t: Throwable) {
                val error = "Server video upload failed: ${t.message}"
                Log.e("RegisterViewModel", error)
                onError(error)
            }
        })
    }
}
