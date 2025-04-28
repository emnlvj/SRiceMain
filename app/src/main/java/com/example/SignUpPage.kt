package com.example.sricedemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpPage : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        val signup: Button = findViewById(R.id.signup_button)
        signup.setOnClickListener {
            val intent = Intent(this@SignUpPage, LoginPage::class.java)
            startActivity(intent)
            finish()

        }
    }
}
