package com.srice

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginSignup : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        makeFullscreen()
        applyCardBackground()
        setupLoginSignupToggle()
        setupGoogleSignIn()
        setupFacebookLogin()
        setupButtons()
    }

    private fun makeFullscreen() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    private fun applyCardBackground() {
        val isDark = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val background = LayerDrawable(arrayOf(
            ColorDrawable(Color.parseColor(if (isDark) "#000000" else "#FFFFFF")),
            if (isDark) StickerScatterDrawableWhite(this) else StickerScatterDrawable(this)
        ))
        findViewById<View>(R.id.appBackground).background = background
    }

    private fun setupLoginSignupToggle() {
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val loginLayout = findViewById<LinearLayout>(R.id.loginLayout)
        val signupLayout = findViewById<LinearLayout>(R.id.signupLayout)

        btnLogin.setOnClickListener {
            signupLayout.fadeOutAndHide()
            loginLayout.fadeInAndShow()
            toggleButtonColors(btnLogin, btnSignup)
        }

        btnSignup.setOnClickListener {
            loginLayout.fadeOutAndHide()
            signupLayout.fadeInAndShow()
            toggleButtonColors(btnSignup, btnLogin)
        }
    }

    private fun toggleButtonColors(active: Button, inactive: Button) {
        active.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green_4caf50))
        active.setTextColor(Color.WHITE)
        inactive.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.teal_light))
        inactive.setTextColor(ContextCompat.getColor(this, R.color.green_4caf50))
    }

    private fun View.fadeOutAndHide() {
        animate().alpha(0f).translationY(50f).setDuration(300).withEndAction {
            visibility = View.GONE
            alpha = 1f
            translationY = 0f
        }
    }

    private fun View.fadeInAndShow() {
        visibility = View.VISIBLE
        alpha = 0f
        translationY = 50f
        animate().alpha(1f).translationY(0f).setDuration(300).start()
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account = task.result
                    val tokenId = account.idToken
                    val credential = GoogleAuthProvider.getCredential(tokenId, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                val googleAuthRequest = GoogleAuthRequest(tokenId ?: "")
                                RetrofitClient.apiService.googleSignIn(googleAuthRequest)
                                    .enqueue(object : Callback<GoogleAuthResponse> {
                                        override fun onResponse(call: Call<GoogleAuthResponse>, response: Response<GoogleAuthResponse>) {
                                            Toast.makeText(applicationContext, response.body()?.message ?: "Signed in", Toast.LENGTH_LONG).show()
                                            startActivity(Intent(this@LoginSignup, Dashboard::class.java))
                                            finish()
                                        }

                                        override fun onFailure(call: Call<GoogleAuthResponse>, t: Throwable) {
                                            Toast.makeText(applicationContext, "Backend failed: ${t.message}", Toast.LENGTH_LONG).show()
                                        }
                                    })
                            } else {
                                Toast.makeText(applicationContext, "Firebase authentication failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(applicationContext, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<MaterialButton>(R.id.btnGoogle).setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
        }
    }

    private fun setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()

        findViewById<MaterialButton>(R.id.btnFacebook).setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        }

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val request = com.facebook.GraphRequest.newMeRequest(loginResult.accessToken) { jsonObject, _ ->
                    val name = jsonObject?.optString("name") ?: ""
                    val email = jsonObject?.optString("email") ?: ""


                    if (name.isNotEmpty() && email.isNotEmpty()) {
                        // Create FacebookAuthRequest instead of User
                        val facebookAuthRequest = FacebookAuthRequest(name, email)

                        RetrofitClient.apiService.facebookLogin(facebookAuthRequest)
                            .enqueue(object : Callback<FacebookAuthResponse> {
                                override fun onResponse(call: Call<FacebookAuthResponse>, response: Response<FacebookAuthResponse>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(applicationContext, response.body()?.message ?: "Signed in", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(this@LoginSignup, Dashboard::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(applicationContext, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onFailure(call: Call<FacebookAuthResponse>, t: Throwable) {
                                    Toast.makeText(applicationContext, "Backend failed: ${t.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                    } else {
                        Toast.makeText(applicationContext, "Failed to retrieve Facebook user data", Toast.LENGTH_SHORT).show()
                    }
                }

                val params = Bundle()
                params.putString("fields", "name,email")
                request.parameters = params
                request.executeAsync()
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "Facebook login cancelled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(applicationContext, "Facebook login error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun setupButtons() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { onBackPressed() }

        findViewById<Button>(R.id.loginButtonTransfer).setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        findViewById<Button>(R.id.signupButton).setOnClickListener {
            val fullName = findViewById<TextInputEditText>(R.id.fullNameEditText).text.toString().trim()
            val email = findViewById<TextInputEditText>(R.id.emailEditText).text.toString().trim()
            val phone = findViewById<TextInputEditText>(R.id.phoneEditText).text.toString().trim()
            val password = findViewById<TextInputEditText>(R.id.passwordEditText).text.toString()
            val confirmPassword = findViewById<TextInputEditText>(R.id.confirmPassword).text.toString()
            val termsCheckbox = findViewById<CheckBox>(R.id.terms_checkbox)

            when {
                fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ->
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()

                password != confirmPassword ->
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()

                !termsCheckbox.isChecked ->
                    Toast.makeText(this, "You must accept the terms", Toast.LENGTH_SHORT).show()

                else -> {
                    val user = User(fullName, email, phone, password)
                    registerUser(user)
                }
            }
        }
    }

    private fun registerUser(user: User) {
        RetrofitClient.apiService.registerUser(user)
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    Toast.makeText(applicationContext, response.body()?.message ?: "Registration successful", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Registration failed: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}