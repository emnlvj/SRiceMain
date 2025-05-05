package com.srice
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/register")
    fun registerUser(@Body user: User): Call<ApiResponse>

    @POST("/api/auth/google")
    fun googleSignIn(@Body googleAuthRequest: GoogleAuthRequest): Call<GoogleAuthResponse>

    @POST("facebook/login")
    fun facebookLogin(@Body request: FacebookAuthRequest): Call<FacebookAuthResponse>
}
