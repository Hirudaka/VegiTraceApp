package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.vegitrace.databinding.ActivityLoginBinding

class shopLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { authResult ->
                        if (authResult.isSuccessful) {
                            // User successfully logged in
                            val userId = authResult.result?.user?.uid
                            if (userId != null) {
                                val intent = Intent(this, FarmerLocation::class.java)
                                intent.putExtra("userId", userId)
                                startActivity(intent)
                                finish() // Close the login activity
                            }
                        } else {
                            Toast.makeText(
                                this, "Login failed. ${authResult.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupRedirectText.setOnClickListener {
            val signupIntent = Intent(this, shopReg::class.java)
            startActivity(signupIntent)
        }
    }
}
