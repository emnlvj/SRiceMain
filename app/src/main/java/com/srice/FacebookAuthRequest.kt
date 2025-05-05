package com.srice

data class FacebookAuthRequest(
    val name: String,
    val email: String,
    val platform: String = "facebook" // Add a platform field to differentiate Facebook login
)

