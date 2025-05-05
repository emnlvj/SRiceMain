package com.srice

data class User(
    val fullName: String,
    val email: String,
    val phone: String = "N/A",      // placeholder if not available
    val password: String = "google" // or some dummy string
)
