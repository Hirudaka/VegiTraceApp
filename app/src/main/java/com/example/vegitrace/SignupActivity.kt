package com.example.vegitrace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.vegitrace.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()
            val name = binding.name.text.toString()
            val address = binding.address.text.toString()
            val phoneNumber = binding.phone.text.toString()
            val vehicleRegNo = binding.regno.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    // Attempt to create a new user with email and password
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                        if (authResult.isSuccessful) {
                            // Registration was successful, now we proceed to save data to Realtime Database
                            val userId = authResult.result?.user?.uid
                            if (userId != null) {
                                val farmer = Farmer(name, email, address, phoneNumber, vehicleRegNo)
                                saveFarmerToDatabase(userId, farmer)

                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Failed to create user.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Registration failed, don't add the user to Firebase Authentication
                            val errorMessage = authResult.exception?.message
                            Log.e("Firebase Error", errorMessage ?: "Unknown error")
                            Toast.makeText(this, "Registration failed. $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Passwords do not match
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Fields are empty
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun saveFarmerToDatabase(userId: String, farmer: Farmer) {
        val farmerRef = database.getReference("farmer").child(userId)
        farmerRef.setValue(farmer).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Database", "Data saved successfully")
            } else {
                val errorMessage = task.exception?.message
                Log.e("Database Error", errorMessage ?: "Unknown error")
            }
        }
    }
}

data class Farmer(
    val name: String,
    val email: String,
    val address: String,
    val phoneNumber: String,
    val vehicleRegNo: String
)
