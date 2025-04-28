package com.example.sricedemo

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LoginSignup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)
        val backButton: ImageButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressed() // This will take the user back to the previous screen
        }
        setupLoginSignupToggle()
        applyCardBackground()
    }

    private fun setupLoginSignupToggle() {
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val signupButton = findViewById<Button>(R.id.btnSignup)
        val loginLayout = findViewById<LinearLayout>(R.id.loginLayout)
        val signupLayout = findViewById<LinearLayout>(R.id.signupLayout)

        loginButton.setOnClickListener {
            if (signupLayout.visibility == View.VISIBLE) {
                signupLayout.animate()
                    .alpha(0f)
                    .translationY(50f)
                    .setDuration(300)
                    .withEndAction {
                        signupLayout.visibility = View.GONE
                        signupLayout.alpha = 1f
                        signupLayout.translationY = 0f
                    }
            }
            loginLayout.visibility = View.VISIBLE
            loginLayout.alpha = 0f
            loginLayout.translationY = 50f
            loginLayout.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .start()

            loginButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green_4caf50))
            signupButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.teal_light))

            loginButton.setTextColor(Color.WHITE)
            signupButton.setTextColor(ContextCompat.getColor(this, R.color.green_4caf50))
        }

        signupButton.setOnClickListener {
            if (loginLayout.visibility == View.VISIBLE) {
                loginLayout.animate()
                    .alpha(0f)
                    .translationY(50f)
                    .setDuration(300)
                    .withEndAction {
                        loginLayout.visibility = View.GONE
                        loginLayout.alpha = 1f
                        loginLayout.translationY = 0f
                    }
            }
            signupLayout.visibility = View.VISIBLE
            signupLayout.alpha = 0f
            signupLayout.translationY = 50f
            signupLayout.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .start()

            signupButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green_4caf50))
            loginButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.teal_light))

            signupButton.setTextColor(Color.WHITE)
            loginButton.setTextColor(ContextCompat.getColor(this, R.color.green_4caf50))
        }
    }

    private fun applyCardBackground() {
        val appView = findViewById<View>(R.id.appBackground)

        val isDark = (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        val background = LayerDrawable(
            arrayOf(
                ColorDrawable(Color.parseColor(if (isDark) "#000000" else "#FFFFFF")),
                if (isDark) StickerScatterDrawableWhite(this) else StickerScatterDrawable(this)
            )
        )

        appView.background = background
    }
}
